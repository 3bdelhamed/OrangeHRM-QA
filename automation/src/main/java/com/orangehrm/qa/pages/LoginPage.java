package com.orangehrm.qa.pages;

import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing the OrangeHRM Login page.
 *
 * Locators: uses @FindBy with CSS selectors for resilience.
 * No assertions. All return types support method chaining or data retrieval.
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────

    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = ".oxd-alert-content-text")
    private WebElement invalidCredentialsAlert;

    @FindBy(css = ".oxd-input-field-error-message")
    private WebElement fieldValidationError;

    @FindBy(css = ".oxd-text.oxd-text--h5.orangehrm-login-title")
    private WebElement loginPageTitle;

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Enters username, password, and clicks login. Returns DashboardPage on success.
     */
    public DashboardPage loginWithValidCredentials(String username, String password) {
        log.info("Attempting login with username: [{}]", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        WaitUtil.getWait(25).until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(AppConstants.DASHBOARD_URL_FRAGMENT));
        WaitUtil.waitForVisibility(By.cssSelector("h6.oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module"));
        log.info("Login successful — navigated to Dashboard");
        return new DashboardPage();
    }

    /**
     * Attempts login with invalid credentials (stays on LoginPage).
     */
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        log.info("Attempting login with invalid credentials — username: [{}]", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    /**
     * Clicks login without entering any credentials (for validation testing).
     */
    public LoginPage clickLoginWithoutCredentials() {
        log.info("Clicking login button without credentials");
        clickLoginButton();
        return this;
    }

    public LoginPage enterUsername(String username) {
        log.debug("Entering username: [{}]", username);
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.debug("Entering password");
        type(passwordInput, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        log.debug("Clicking Login button");
        click(loginButton);
        return this;
    }

    // ── State queries (used by assertions in test classes) ────────────────────

    public boolean isLoginPageDisplayed() {
        try {
            waitForVisibility(loginButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getInvalidCredentialsMessage() {
        waitForVisibility(invalidCredentialsAlert);
        return getText(invalidCredentialsAlert);
    }

    public String getFieldValidationError() {
        waitForVisibility(fieldValidationError);
        return getText(fieldValidationError);
    }

    public boolean isInvalidCredentialAlertVisible() {
        try {
            waitForVisibility(invalidCredentialsAlert);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFieldErrorVisible() {
        try {
            waitForVisibility(fieldValidationError);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
