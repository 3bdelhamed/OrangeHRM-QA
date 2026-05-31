package com.orangehrm.qa.tests;

import com.orangehrm.qa.base.BaseTest;
import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 * Smoke tests for the Dashboard module.
 * Validates that the main application landing page loads correctly
 * and navigation links are available.
 */
@Epic("Core Navigation")
@Feature("Dashboard")
public class DashboardTests extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest(Method method) {
        dashboardPage = loginAsAdmin();
    }

    @Test(groups = {AppConstants.SMOKE})
    @Story("Dashboard Load")
    @Description("Verify that the Dashboard page loads successfully after admin login")
    @Severity(SeverityLevel.BLOCKER)
    public void testDashboardLoadsAfterLogin() {
        log.info("TC-D01: Verify Dashboard loads after login");

        assertTrue(dashboardPage.isDashboardLoaded(),
                "Dashboard should be visible after login");
        assertTrue(dashboardPage.getCurrentUrl().contains(AppConstants.DASHBOARD_URL_FRAGMENT),
                "URL should contain 'dashboard'");

        log.info("TC-D01: PASSED");
    }

    @Test(groups = {AppConstants.SMOKE})
    @Story("Module Navigation")
    @Description("Verify that the PIM module is accessible from the navigation sidebar")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigationToPIM() {
        log.info("TC-D02: Navigate to PIM from Dashboard");

        dashboardPage.navigateToPIM();

        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.PIM_URL_FRAGMENT),
                "Should navigate to PIM Employee List page");

        log.info("TC-D02: PASSED");
    }

    @Test(groups = {AppConstants.SMOKE})
    @Story("Module Navigation")
    @Description("Verify that the Admin module is accessible from the navigation sidebar")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigationToAdmin() {
        log.info("TC-D03: Navigate to Admin from Dashboard");

        dashboardPage.navigateToAdmin();

        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.ADMIN_URL_FRAGMENT),
                "Should navigate to Admin System Users page");

        log.info("TC-D03: PASSED");
    }

    @Test(groups = {AppConstants.SMOKE})
    @Story("Module Navigation")
    @Description("Verify that the Leave module is accessible from the navigation sidebar")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigationToLeave() {
        log.info("TC-D04: Navigate to Leave from Dashboard");

        dashboardPage.navigateToLeave();

        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.LEAVE_URL_FRAGMENT),
                "Should navigate to Leave List page");

        log.info("TC-D04: PASSED");
    }

    @Test(groups = {AppConstants.SMOKE})
    @Story("Logout Flow")
    @Description("Verify that a logged-in admin user can successfully log out from the Dashboard")
    @Severity(SeverityLevel.CRITICAL)
    public void testLogoutFromDashboard() {
        log.info("TC-D05: Verify logout from Dashboard");

        LoginPage loginPage = dashboardPage.logout();

        assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logging out");
        assertTrue(loginPage.getCurrentUrl().contains("login"),
                "URL should redirect to the login screen");

        log.info("TC-D05: PASSED");
    }
}
