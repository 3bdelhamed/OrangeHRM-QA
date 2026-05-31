package com.orangehrm.qa.tests;

import com.orangehrm.qa.base.BaseTest;
import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.pages.AdminPage;
import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.data.TestDataFactory;
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
 * Test suite for the Admin module — System User management.
 *
 * Coverage:
 * - [Regression] Admin page loads with correct heading
 * - [Regression] System User list has records
 * - [Regression] Add User form is accessible
 * - [Regression] Search by username returns results
 * - [Regression] Search by non-existent username returns no results
 */
@Epic("Administration")
@Feature("Admin Module - System Users")
public class AdminTests extends BaseTest {

    private AdminPage adminPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToAdmin(Method method) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                DashboardPage dashboard = loginAsAdmin();
                adminPage = dashboard.navigateToAdmin();
                if (adminPage.isAdminPageLoaded()) {
                    return;
                }
            } catch (Exception e) {
                log.warn("Navigation to Admin failed (attempt {}): {}. Retrying...", attempts + 1, e.getMessage());
            }
            attempts++;
            if (attempts >= 3) {
                fail("Failed to navigate to Admin page after 3 attempts");
            }
            try {
                getDriver().navigate().refresh();
            } catch (Exception ne) {
                log.error("Failed to refresh: {}", ne.getMessage());
            }
        }
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.ADMIN})
    @Story("System User List")
    @Description("Verify the Admin System Users page loads and is accessible")
    @Severity(SeverityLevel.CRITICAL)
    public void testAdminPageLoads() {
        log.info("TC-A01: Verify Admin System Users page loads");

        assertTrue(adminPage.isAdminPageLoaded(),
                "Admin page should load successfully");
        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.ADMIN_URL_FRAGMENT),
                "URL should contain admin fragment");

        log.info("TC-A01: PASSED");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.ADMIN})
    @Story("System User List")
    @Description("Verify the System Users table contains at least one record")
    @Severity(SeverityLevel.NORMAL)
    public void testSystemUsersTableHasRecords() {
        log.info("TC-A02: Verify system user table is populated");

        int count = adminPage.getUserTableRowCount();
        assertTrue(count > 0,
                "System users table should have at least one record (Admin). Found: " + count);

        log.info("TC-A02: PASSED — Found {} user records", count);
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.ADMIN})
    @Story("Add System User")
    @Description("Verify that the Add User form is accessible from the Admin page")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddUserFormIsAccessible() {
        log.info("TC-A03: Verify Add User form opens");

        adminPage.clickAddUser();

        assertTrue(adminPage.isAddUserFormDisplayed(),
                "Add User form should be visible");
        assertTrue(getDriver().getCurrentUrl().contains("saveSystemUser"),
                "URL should contain 'saveSystemUser'");

        log.info("TC-A03: PASSED — Add User form is accessible");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.ADMIN})
    @Story("Search System User")
    @Description("Verify that searching for 'Admin' username returns at least one result")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchForAdminUserReturnsResults() {
        log.info("TC-A04: Search for 'Admin' user");

        adminPage.searchByUsername("Admin");

        assertTrue(adminPage.hasUserResults(),
                "Searching for 'Admin' should return at least one result");

        log.info("TC-A04: PASSED — Admin user found in search results");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.ADMIN})
    @Story("Search System User")
    @Description("Verify that searching by a random non-existent username returns no matching records")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchForNonExistentUserReturnsNoResults() {
        log.info("TC-A05: Search for non-existent username");

        String nonExistentUsername = "ZZZZ_NO_EXIST_" + TestDataFactory.generateRandomDigits(6);
        adminPage.searchByUsername(nonExistentUsername);

        // No results expected — table body should either be empty or show "No Records Found"
        int count = adminPage.getUserTableRowCount();
        assertEquals(count, 0,
                "Search for non-existent user should return 0 results. Found: " + count);

        log.info("TC-A05: PASSED — No results for non-existent username");
    }
}
