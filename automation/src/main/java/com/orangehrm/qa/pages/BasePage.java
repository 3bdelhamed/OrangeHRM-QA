package com.orangehrm.qa.pages;

import com.orangehrm.qa.drivers.DriverFactory;
import com.orangehrm.qa.utils.ElementActions;
import com.orangehrm.qa.utils.WaitUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    protected void click(WebElement element) {
        ElementActions.click(element);
    }

    protected void click(By locator) {
        ElementActions.click(locator);
    }

    protected void type(WebElement element, String text) {
        ElementActions.type(element, text);
    }

    protected void clearAndType(WebElement element, String text) {
        ElementActions.clearAndType(element, text);
    }

    protected String getText(WebElement element) {
        return ElementActions.getText(element);
    }

    protected boolean isDisplayed(WebElement element) {
        return ElementActions.isDisplayed(element);
    }

    protected WebElement waitForVisibility(WebElement element) {
        return WaitUtil.waitForVisibility(element);
    }

    protected WebElement waitForClickability(WebElement element) {
        return WaitUtil.waitForClickability(element);
    }

    protected boolean waitForUrlContains(String fragment) {
        return WaitUtil.waitForUrlContains(fragment);
    }

    protected boolean isElementPresent(By locator) {
        return WaitUtil.isElementPresent(locator);
    }

    @FindBy(css = ".oxd-toast-content span.oxd-text--toast-message")
    private WebElement commonToastMessage;

    public String getToastMessage() {
        log.info("Waiting for toast message to appear");
        WaitUtil.waitForToastMessage(By.cssSelector(".oxd-toast-content span.oxd-text--toast-message"));
        String text = getText(commonToastMessage);
        log.info("Retrieved toast message: [{}]", text);
        return text;
    }
}
