package com.orangehrm.qa.utils;

import com.orangehrm.qa.config.ConfigReader;
import com.orangehrm.qa.drivers.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Centralized explicit wait utilities. All waits are driven from ConfigReader
 * so timeouts can be tuned per environment without touching code.
 */
public final class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);
    private static final int DEFAULT_TIMEOUT = ConfigReader.getInstance().getExplicitWait();

    private WaitUtil() {}

    // ── Reusable WebDriverWait factory ────────────────────────────────────────

    public static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    // ── Spinner / Loader handling ─────────────────────────────────────────────

    public static void waitForSpinnersToDisappear() {
        try {
            By spinnerLocator = By.cssSelector(".oxd-loading-spinner, .oxd-form-loader");
            WebDriver driver = DriverFactory.getDriver();
            if (!driver.findElements(spinnerLocator).isEmpty()) {
                log.debug("Loader/Spinner detected. Waiting for invisibility...");
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
            }
        } catch (Exception e) {
            log.warn("Error while waiting for spinner/loader to disappear: {}", e.getMessage());
        }
    }

    // ── Visibility waits ──────────────────────────────────────────────────────

    public static WebElement waitForVisibility(WebElement element) {
        log.debug("Waiting for element visibility: {}", element);
        waitForSpinnersToDisappear();
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of locator: {}", locator);
        waitForSpinnersToDisappear();
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static List<WebElement> waitForAllVisible(List<WebElement> elements) {
        waitForSpinnersToDisappear();
        return getWait().until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    // ── Clickability waits ────────────────────────────────────────────────────

    public static WebElement waitForClickability(WebElement element) {
        log.debug("Waiting for element to be clickable");
        waitForSpinnersToDisappear();
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickability(By locator) {
        waitForSpinnersToDisappear();
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ── Text / URL waits ──────────────────────────────────────────────────────

    public static boolean waitForUrlContains(String fragment) {
        log.debug("Waiting for URL to contain: {}", fragment);
        return getWait().until(ExpectedConditions.urlContains(fragment));
    }

    public static boolean waitForTitleContains(String titleFragment) {
        return getWait().until(ExpectedConditions.titleContains(titleFragment));
    }

    public static boolean waitForTextPresent(WebElement element, String text) {
        log.debug("Waiting for text '{}' in element", text);
        return getWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // ── Element state waits ───────────────────────────────────────────────────

    public static boolean waitForInvisibility(WebElement element) {
        log.debug("Waiting for element to become invisible");
        return getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ── Staleness / refresh waits ─────────────────────────────────────────────

    public static boolean waitForStaleness(WebElement element) {
        return getWait().until(ExpectedConditions.stalenessOf(element));
    }

    // ── Fluent wait for toast messages (short-lived elements) ─────────────────

    public static WebElement waitForToastMessage(By locator) {
        return getWait(5).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static boolean isElementPresent(By locator) {
        log.debug("Checking presence of element: {}", locator);
        try {
            return !DriverFactory.getDriver().findElements(locator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
