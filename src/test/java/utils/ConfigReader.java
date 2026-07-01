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
        return bootstrapProps.getProperty("project.folder", "./test-output");
    }

    public String getAppUrl() {
        return getRequired("app.url");
    }

    public String getProjectFolder() {
        return getRequired("project.folder");
    }

    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "20"));
    }

    public String get(String key) {
        return getRequired(key);
    }

    private String getRequired(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config property: " + key);
        }
        return value;
    }
}
