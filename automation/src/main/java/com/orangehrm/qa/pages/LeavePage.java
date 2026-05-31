package com.orangehrm.qa.pages;

import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the Leave Management module.
 * Covers: Leave List, Apply Leave, and Leave Entitlements navigation.
 */
public class LeavePage extends BasePage {

    // ── Navigation locators ───────────────────────────────────────────────────

    @FindBy(css = "h5.oxd-text--h5")
    private WebElement pageHeading;

    @FindBy(xpath = "//nav[@class='oxd-topbar-body-nav']//a[text()='Apply']")
    private WebElement applyLeaveLink;

    @FindBy(xpath = "//nav[@class='oxd-topbar-body-nav']//span[contains(text(),'Entitlements')]")
    private WebElement entitlementsDropdown;

    @FindBy(xpath = "//a[text()='Employee Entitlements']")
    private WebElement employeeEntitlementsLink;

    @FindBy(xpath = "//nav[@class='oxd-topbar-body-nav']//a[text()='Leave List']")
    private WebElement leaveListLink;

    // ── Apply Leave form & Search locators ────────────────────────────────────

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[contains(.,'Leave Type')]]//div[contains(@class,'oxd-select-text')]")
    private WebElement leaveTypeDropdown;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[contains(.,'From Date')]]//input")
    private WebElement fromDateInput;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[contains(.,'To Date')]]//input")
    private WebElement toDateInput;

    @FindBy(css = "textarea.oxd-textarea")
    private WebElement commentTextarea;

    @FindBy(css = "button[type='submit'].oxd-button--secondary")
    private WebElement applyButton;

    @FindBy(css = "button[type='submit'].oxd-button--secondary")
    private WebElement searchButton;

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private List<WebElement> leaveRows;

    // ── Actions ───────────────────────────────────────────────────────────────

    public LeavePage clickApplyLeave() {
        log.info("Clicking 'Apply Leave' navigation link");
        click(applyLeaveLink);
        WaitUtil.waitForUrlContains("applyLeave");
        WaitUtil.waitForSpinnersToDisappear();
        return this;
    }

    public LeavePage clickLeaveList() {
        log.info("Clicking 'Leave List' navigation link");
        click(leaveListLink);
        return this;
    }

    public LeavePage clickLeaveEntitlement() {
        log.info("Navigating to Leave Entitlements");
        click(entitlementsDropdown);
        click(employeeEntitlementsLink);
        return this;
    }

    public LeavePage selectLeaveType(String leaveType) {
        log.debug("Selecting leave type: {}", leaveType);
        click(leaveTypeDropdown);
        try {
            Thread.sleep(300); // Wait for dropdown animation to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        By optionLocator = By.xpath(
                "//div[@role='option'][.//span[text()='" + leaveType + "']]"
        );
        com.orangehrm.qa.utils.ElementActions.click(optionLocator);
        return this;
    }

    public LeavePage enterFromDate(String date) {
        log.debug("Entering from date: {}", date);
        // Clear value before typing
        clearAndType(fromDateInput, date);
        return this;
    }

    public LeavePage enterToDate(String date) {
        log.debug("Entering to date: {}", date);
        clearAndType(toDateInput, date);
        return this;
    }

    public LeavePage enterComment(String comment) {
        log.debug("Entering leave comment");
        type(commentTextarea, comment);
        return this;
    }

    public LeavePage clickApplyButton() {
        log.info("Submitting leave application");
        click(applyButton);
        return this;
    }

    public LeavePage applyLeave(String leaveType, String fromDate, String toDate, String comment) {
        clickApplyLeave();
        selectLeaveType(leaveType);
        enterFromDate(fromDate);
        enterToDate(toDate);
        enterComment(comment);
        clickApplyButton();
        return this;
    }

    public LeavePage searchLeaves() {
        log.info("Searching leave records");
        click(searchButton);
        return this;
    }

    // ── State queries ─────────────────────────────────────────────────────────

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isLeaveListLoaded() {
        try {
            waitForUrlContains(AppConstants.LEAVE_URL_FRAGMENT);
            waitForVisibility(pageHeading);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isApplyLeaveFormDisplayed() {
        try {
            waitForUrlContains("applyLeave");
            WaitUtil.waitForVisibility(By.cssSelector("button[type='submit']"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addEntitlementIfNone(String leaveType, String amount) {
        log.info("Checking if leave entitlements need to be added...");
        clickApplyLeave();
        
        WaitUtil.waitForSpinnersToDisappear();
        
        By dropdownLocator = By.xpath("//div[contains(@class,'oxd-input-group')][.//label[contains(.,'Leave Type')]]//div[contains(@class,'oxd-select-text')]");
        By noBalanceLocator = By.xpath("//p[contains(.,'No Leave Types with Leave Balance')]");
        
        log.info("Waiting for Apply Leave form or No Balance message to render...");
        try {
            WaitUtil.getWait(10).until(d -> {
                try {
                    return !d.findElements(dropdownLocator).isEmpty() && d.findElement(dropdownLocator).isDisplayed() ||
                           !d.findElements(noBalanceLocator).isEmpty() && d.findElement(noBalanceLocator).isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            log.warn("Timed out waiting for Apply Leave state to resolve: {}", e.getMessage());
        }
        
        boolean noBalanceMsg = isElementPresent(noBalanceLocator);
        if (noBalanceMsg) {
            log.info("No leave balance message detected. Adding entitlement dynamically...");
            
            String profileName = driver.findElement(By.cssSelector(".oxd-userdropdown-name")).getText().trim();
            log.info("Current user profile name: {}", profileName);
            
            driver.navigate().to(driver.getCurrentUrl().replaceAll("/leave/applyLeave", "/leave/addLeaveEntitlement"));
            WaitUtil.waitForSpinnersToDisappear();
            
            WebElement empNameInput = WaitUtil.waitForVisibility(By.xpath("//div[contains(@class,'oxd-input-group')][.//label[contains(.,'Employee Name')]]//input"));
            empNameInput.sendKeys(profileName);
            
            By hintOptionLocator = By.xpath("//div[@role='option']");
            WaitUtil.waitForVisibility(hintOptionLocator);
            try {
                Thread.sleep(500); // Wait for autocomplete hints to stabilize
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            click(hintOptionLocator);
            
            WebElement ltDropdown = WaitUtil.waitForVisibility(By.xpath("//div[contains(@class,'oxd-input-group')][.//label[contains(.,'Leave Type')]]//div[contains(@class,'oxd-select-text')]"));
            click(ltDropdown);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            By optionLocator = By.xpath("//div[@role='option'][.//span[text()='" + leaveType + "']]");
            click(optionLocator);
            
            WebElement entitlementInput = WaitUtil.waitForVisibility(By.xpath("//div[contains(@class,'oxd-input-group')][.//label[contains(.,'Entitlement')]]//input"));
            clearAndType(entitlementInput, amount);
            
            WebElement saveBtn = WaitUtil.waitForClickability(By.cssSelector("button[type='submit']"));
            click(saveBtn);
            WaitUtil.waitForSpinnersToDisappear();
            
            By confirmBtnLocator = By.xpath("//button[contains(.,'Confirm')]");
            try {
                WebElement confirmBtn = WaitUtil.getWait(5).until(
                        org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(confirmBtnLocator)
                );
                log.info("Confirm dialog detected. Confirming entitlement...");
                click(confirmBtn);
                WaitUtil.waitForSpinnersToDisappear();
            } catch (Exception e) {
                log.info("No confirmation dialog detected or timeout: {}", e.getMessage());
            }
            
            log.info("Leave entitlement added successfully.");
            
            clickApplyLeave();
        } else {
            log.info("Leave balance is already available. Proceeding.");
        }
    }

    public int getLeaveRecordCount() {
        try {
            WaitUtil.getWait(5).until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.cssSelector(".oxd-table-body .oxd-table-row")));
        } catch (Exception e) {
            // Ignore
        }
        return leaveRows.size();
    }
}
