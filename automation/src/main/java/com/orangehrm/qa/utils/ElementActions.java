package com.orangehrm.qa.utils;

import com.orangehrm.qa.drivers.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * Wrapper around Selenium interactions providing:
 * - Consistent explicit waits before every action
 * - Centralized logging of all element interactions
 * - Resilient click/type strategies (JS fallback)
 */
public final class ElementActions {

    private static final Logger log = LogManager.getLogger(ElementActions.class);

    private ElementActions() {}

    // ── Click ─────────────────────────────────────────────────────────────────

    public static void click(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WaitUtil.waitForClickability(element);
                log.debug("Clicking element: {}", element);
                element.click();
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                log.warn("Stale element reference caught. Retrying click... attempt {}", attempts + 1);
                attempts++;
            }
        }
        throw new org.openqa.selenium.StaleElementReferenceException("Element remained stale after 3 attempts");
    }

    public static void click(By locator) {
        WaitUtil.waitForClickability(locator);
        log.debug("Clicking locator: {}", locator);
        DriverFactory.getDriver().findElement(locator).click();
    }

    /** Falls back to JS click when normal click is intercepted by overlays. */
    public static void jsClick(WebElement element) {
        log.debug("JS clicking element: {}", element);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    // ── Type / Input ──────────────────────────────────────────────────────────

    public static void type(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WaitUtil.waitForVisibility(element);
                log.debug("Typing '{}' into element", text);
                element.clear();
                element.sendKeys(text);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                log.warn("Stale element reference caught. Retrying type... attempt {}", attempts + 1);
                attempts++;
            }
        }
        throw new org.openqa.selenium.StaleElementReferenceException("Element remained stale after 3 attempts");
    }

    public static void typeAndSubmit(WebElement element, String text) {
        type(element, text);
        element.sendKeys(Keys.ENTER);
    }

    public static void clearAndType(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WaitUtil.waitForClickability(element);
                element.click();
                
                // Select all and delete using standard keys
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                element.sendKeys(Keys.BACK_SPACE);
                
                // Fallback: if value is still not empty, use JS to clear it
                String val = element.getAttribute("value");
                if (val != null && !val.isEmpty()) {
                    JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
                    js.executeScript("arguments[0].value = '';", element);
                    js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", element);
                    js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
                }
                
                element.sendKeys(text);
                element.sendKeys(Keys.TAB); // close calendar popup by tab-out and commit value
                log.debug("Cleared and typed '{}' into element", text);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                log.warn("Stale element reference caught. Retrying clearAndType... attempt {}", attempts + 1);
                attempts++;
            }
        }
        throw new org.openqa.selenium.StaleElementReferenceException("Element remained stale after 3 attempts");
    }

    // ── Dropdown / Select ─────────────────────────────────────────────────────

    public static void selectByVisibleText(WebElement element, String visibleText) {
        WaitUtil.waitForVisibility(element);
        log.debug("Selecting '{}' from dropdown", visibleText);
        new Select(element).selectByVisibleText(visibleText);
    }

    public static void selectByValue(WebElement element, String value) {
        WaitUtil.waitForVisibility(element);
        new Select(element).selectByValue(value);
    }

    // ── Text retrieval ────────────────────────────────────────────────────────

    public static String getText(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WaitUtil.waitForVisibility(element);
                String text = element.getText().trim();
                log.debug("Got text: '{}'", text);
                return text;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                log.warn("Stale element reference caught. Retrying getText... attempt {}", attempts + 1);
                attempts++;
            }
        }
        throw new org.openqa.selenium.StaleElementReferenceException("Element remained stale after 3 attempts");
    }

    public static String getAttribute(WebElement element, String attribute) {
        WaitUtil.waitForVisibility(element);
        return element.getAttribute(attribute);
    }

    // ── State checks ──────────────────────────────────────────────────────────

    public static boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Scroll utilities ──────────────────────────────────────────────────────

    public static void scrollToElement(WebElement element) {
        log.debug("Scrolling to element");
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public static void scrollToTop() {
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript("window.scrollTo(0, 0);");
    }

    // ── Hover ─────────────────────────────────────────────────────────────────

    public static void hover(WebElement element) {
        log.debug("Hovering over element");
        new Actions(DriverFactory.getDriver()).moveToElement(element).perform();
    }

    // ── JavaScript helpers ────────────────────────────────────────────────────

    public static void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        js.executeScript(
                "arguments[0].style.border='3px solid red'; arguments[0].style.backgroundColor='yellow';",
                element
        );
    }

    public static Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(script, args);
    }
}
