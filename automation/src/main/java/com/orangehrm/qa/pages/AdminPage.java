package com.orangehrm.qa.pages;

import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AdminPage extends BasePage {
    @FindBy(css = "h5.oxd-text--h5")
    private WebElement pageHeading;

    @FindBy(css = "button.oxd-button--secondary[type='button']")
    private WebElement addUserButton;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='User Role']]//div[contains(@class,'oxd-select-text')]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Status']]//div[contains(@class,'oxd-select-text')]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Employee Name']]//input")
    private WebElement employeeNameInput;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Username']]//input")
    private WebElement usernameInput;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Password']]//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Confirm Password']]//input[@type='password']")
    private WebElement confirmPasswordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    @FindBy(css = "button[type='submit'].oxd-button--secondary")
    private WebElement searchButton;

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private List<WebElement> userRows;

    public AdminPage clickAddUser() {
        log.info("Clicking 'Add' user button");
        click(addUserButton);
        WaitUtil.waitForUrlContains("saveSystemUser");
        return this;
    }

    public AdminPage selectUserRole(String role) {
        log.debug("Selecting user role: {}", role);
        click(userRoleDropdown);
        try {
            Thread.sleep(300); // Wait for dropdown animation to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        By optionLocator = By.xpath(
                "//div[@role='option'][.//span[text()='" + role + "']]"
        );
        com.orangehrm.qa.utils.ElementActions.click(optionLocator);
        return this;
    }

    public AdminPage selectStatus(String status) {
        log.debug("Selecting status: {}", status);
        click(statusDropdown);
        try {
            Thread.sleep(300); // Wait for dropdown animation to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        By optionLocator = By.xpath(
                "//div[@role='option'][.//span[text()='" + status + "']]"
        );
        com.orangehrm.qa.utils.ElementActions.click(optionLocator);
        return this;
    }

    public AdminPage enterEmployeeName(String name) {
        log.debug("Entering employee name: {}", name);
        type(employeeNameInput, name);
        return this;
    }

    public AdminPage enterUsername(String username) {
        log.debug("Entering username: {}", username);
        type(usernameInput, username);
        return this;
    }

    public AdminPage enterPassword(String password) {
        log.debug("Entering password");
        type(passwordInput, password);
        return this;
    }

    public AdminPage enterConfirmPassword(String password) {
        log.debug("Entering confirm password");
        type(confirmPasswordInput, password);
        return this;
    }

    public AdminPage saveUser() {
        log.info("Saving new system user");
        click(saveButton);
        return this;
    }

    public AdminPage searchByUsername(String username) {
        log.info("Searching system users by username: {}", username);
        type(usernameInput, username);
        click(searchButton);
        return this;
    }

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public boolean isAdminPageLoaded() {
        waitForUrlContains(AppConstants.ADMIN_URL_FRAGMENT);
        try {
            waitForVisibility(pageHeading);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddUserFormDisplayed() {
        try {
            WaitUtil.waitForVisibility(By.xpath("//div[contains(@class,'oxd-input-group')][.//label[text()='Confirm Password']]"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getUserTableRowCount() {
        try {
            WaitUtil.getWait(5).until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.cssSelector(".oxd-table-body .oxd-table-row")));
        } catch (Exception e) {
            // Ignore
        }
        return userRows.size();
    }

    public boolean hasUserResults() {
        return getUserTableRowCount() > 0;
    }
}
