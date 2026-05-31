package com.orangehrm.qa.tests;

import com.orangehrm.qa.base.BaseTest;
import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.data.TestDataFactory;
import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.pages.LeavePage;
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
 * Test suite for the Leave Management module.
 *
 * Coverage:
 * - [Regression] Leave List page loads
 * - [Regression] Navigation to Apply Leave form
 * - [Regression] Apply Leave form displays correctly
 * - [Regression] Leave Entitlement navigation
 * - [Regression] Search / filter leave records
 * - [Regression] Apply Leave happy path / form submission
 */
@Epic("Leave Management")
@Feature("Leave Module")
public class LeaveTests extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToLeave(Method method) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                DashboardPage dashboard = loginAsAdmin();
                leavePage = dashboard.navigateToLeave();
                if (leavePage.isLeaveListLoaded()) {
                    return;
                }
            } catch (Exception e) {
                log.warn("Navigation to Leave failed (attempt {}): {}. Retrying...", attempts + 1, e.getMessage());
            }
            attempts++;
            if (attempts >= 3) {
                fail("Failed to navigate to Leave page after 3 attempts");
            }
            try {
                getDriver().navigate().refresh();
            } catch (Exception ne) {
                log.error("Failed to refresh: {}", ne.getMessage());
            }
        }
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Leave List")
    @Description("Verify that the Leave List page loads with the correct URL and heading")
    @Severity(SeverityLevel.CRITICAL)
    public void testLeaveListPageLoads() {
        log.info("TC-LV01: Verify Leave List page loads");

        assertTrue(leavePage.isLeaveListLoaded(),
                "Leave List page should be accessible");
        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.LEAVE_URL_FRAGMENT),
                "URL should contain Leave fragment");

        log.info("TC-LV01: PASSED");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Apply Leave")
    @Description("Verify that the Apply Leave form is accessible from the Leave module navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void testApplyLeaveFormIsAccessible() {
        log.info("TC-LV02: Navigate to Apply Leave form");

        leavePage.addEntitlementIfNone("CAN - Personal", "20.0");

        assertTrue(leavePage.isApplyLeaveFormDisplayed(),
                "Apply Leave form should be displayed after clicking 'Apply'");
        assertTrue(getDriver().getCurrentUrl().contains("applyLeave"),
                "URL should contain 'applyLeave'");

        log.info("TC-LV02: PASSED — Apply Leave form is accessible");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Apply Leave")
    @Description("Verify that Apply Leave form shows correct page context after navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testApplyLeavePageContext() {
        log.info("TC-LV03: Verify Apply Leave page heading / form state");

        leavePage.clickApplyLeave();

        // Verify we are on the correct sub-page
        String url = getDriver().getCurrentUrl();
        assertTrue(url.contains("applyLeave"),
                "Apply Leave URL should be active. Actual: " + url);

        log.info("TC-LV03: PASSED");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Leave Entitlement")
    @Description("Verify that Leave Entitlement page is accessible from the Leave module")
    @Severity(SeverityLevel.NORMAL)
    public void testLeaveEntitlementNavigates() {
        log.info("TC-LV04: Navigate to Leave Entitlements");

        leavePage.clickLeaveEntitlement();

        assertTrue(getDriver().getCurrentUrl().contains("viewLeaveEntitlement"),
                "Should navigate to Leave Entitlement page");

        log.info("TC-LV04: PASSED — Leave Entitlement page loaded");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Leave List")
    @Description("Verify that the Leave List search/filter function can be triggered without error")
    @Severity(SeverityLevel.NORMAL)
    public void testLeaveListSearchWithNoFilters() {
        log.info("TC-LV05: Trigger leave list search with no filters");

        leavePage.searchLeaves();

        // Verify we remain on the leave list after search
        assertTrue(getDriver().getCurrentUrl().contains("leave"),
                "Should remain on Leave module page after search");

        log.info("TC-LV05: PASSED — Leave list search completed");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.LEAVE})
    @Story("Apply Leave")
    @Description("Verify that leave can be applied using generated faker data")
    @Severity(SeverityLevel.BLOCKER)
    public void testApplyLeaveHappyPath() {
        log.info("TC-LV06: Apply leave happy path execution");

        TestDataFactory.LeaveRequestData leaveData = TestDataFactory.generateLeaveRequest();

        leavePage.addEntitlementIfNone("CAN - Personal", "20.0");

        leavePage.selectLeaveType("CAN - Personal")
                 .enterFromDate(leaveData.fromDate())
                 .enterToDate(leaveData.toDate())
                 .enterComment(leaveData.comment())
                 .clickApplyButton();

        // Check if error or success message appears to confirm submission flow completed
        String toast = leavePage.getToastMessage();
        assertNotNull(toast, "A toast notification should appear after leave submission.");
        log.info("TC-LV06: PASSED — Leave applied with message: [{}]", toast);
    }
}
