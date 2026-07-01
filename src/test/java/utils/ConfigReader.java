package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton loader for src/test/resources/config/config.properties.
 * Loaded exactly once per JVM; all framework classes must read config through this class.
 */
public final class ConfigReader {

    private static final String CONFIG_PATH = "config/config.properties";
    private static volatile ConfigReader instance;
    private final Properties properties;

    static {
        // Must run before any Logback logger is initialized, so the rolling file
        // appender in logback.xml can resolve ${LOG_DIR} to the configured project folder.
        System.setProperty("LOG_DIR", peekProjectFolder());
    }

    private ConfigReader() {
        properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (in == null) {
                throw new IllegalStateException("Could not find " + CONFIG_PATH + " on the classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + CONFIG_PATH, e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /** Reads project.folder directly, without Properties/Logback, for use in the static initializer. */
    private static String peekProjectFolder() {
        Properties bootstrapProps = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (in != null) {
                bootstrapProps.load(in);
            }
        } catch (IOException ignored) {
            // fall through to default below
        }
        return resolve("project.folder", bootstrapProps.getProperty("project.folder", "./test-output"));
    }

    public String getAppUrl() {
        return getRequired("app.url");
    }

    public String getProjectFolder() {
        return getRequired("project.folder");
    }

    public String getBrowser() {
        return resolve("browser", properties.getProperty("browser", "chrome"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(resolve("headless", properties.getProperty("headless", "false")));
    }

    public int getImplicitWait() {
        return Integer.parseInt(resolve("implicit.wait", properties.getProperty("implicit.wait", "10")));
    }

    public int getExplicitWait() {
        return Integer.parseInt(resolve("explicit.wait", properties.getProperty("explicit.wait", "20")));
    }

    public String get(String key) {
        return getRequired(key);
    }

    private String getRequired(String key) {
        String value = resolve(key, properties.getProperty(key));
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config property: " + key);
        }
        return value;
    }

    /**
     * Resolves a config value with CI-friendly precedence: JVM system property
     * (e.g. -Dapp.url=...) > environment variable (e.g. APP_URL) > config.properties.
     * Lets a CI pipeline inject per-environment values without editing tracked files.
     */
    private static String resolve(String key, String fileValue) {
        String systemPropertyValue = System.getProperty(key);
        if (systemPropertyValue != null && !systemPropertyValue.trim().isEmpty()) {
            return systemPropertyValue;
        }

        String envVarValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envVarValue != null && !envVarValue.trim().isEmpty()) {
            return envVarValue;
        }

        return fileValue;
    }
}
