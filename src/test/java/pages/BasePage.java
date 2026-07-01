package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Common plumbing shared by all page objects: PageFactory init and wait helpers.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }
}
