package com.orangehrm.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing the OrangeHRM Dashboard.
 * Handles navigation to all major modules and dashboard state verification.
 */
public class DashboardPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────

    @FindBy(css = "h6.oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module")
    private WebElement dashboardHeading;

    @FindBy(css = "span.oxd-userdropdown-tab")
    private WebElement userDropdown;

    @FindBy(css = "a.oxd-userdropdown-link[href*='logout']")
    private WebElement logoutLink;

    @FindBy(css = "a[href*='pim']")
    private WebElement pimNavLink;

    @FindBy(css = "a[href*='leave']")
    private WebElement leaveNavLink;

    @FindBy(css = "a[href*='admin']")
    private WebElement adminNavLink;

    @FindBy(css = ".oxd-main-menu-item--name")
    private WebElement firstMenuItemLabel;

    // ── Actions ───────────────────────────────────────────────────────────────

    public PIMPage navigateToPIM() {
        log.info("Navigating to PIM module");
        click(pimNavLink);
        return new PIMPage();
    }

    public LeavePage navigateToLeave() {
        log.info("Navigating to Leave module");
        click(leaveNavLink);
        return new LeavePage();
    }

    public AdminPage navigateToAdmin() {
        log.info("Navigating to Admin module");
        click(adminNavLink);
        return new AdminPage();
    }

    public LoginPage logout() {
        log.info("Logging out");
        click(userDropdown);
        click(logoutLink);
        return new LoginPage();
    }

    // ── State queries ─────────────────────────────────────────────────────────

    public boolean isDashboardLoaded() {
        try {
            waitForVisibility(dashboardHeading);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDashboardHeading() {
        return getText(dashboardHeading);
    }

    public String getLoggedInUsername() {
        return getText(userDropdown);
    }
}
