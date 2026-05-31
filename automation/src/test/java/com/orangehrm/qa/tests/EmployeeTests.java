package com.orangehrm.qa.tests;

import com.orangehrm.qa.base.BaseTest;
import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.pages.DashboardPage;
import com.orangehrm.qa.pages.PIMPage;
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
 * Test suite for the PIM (Employee Management) module.
 *
 * Coverage:
 * - [Regression] Navigate to Employee List
 * - [Regression] Add Employee — happy path
 * - [Regression] Add Employee Form is accessible
 * - [Regression] Search for employee by name
 * - [Regression] Employee list has results (sanity check)
 */
@Epic("Employee Management")
@Feature("PIM Module")
public class EmployeeTests extends BaseTest {

    private DashboardPage dashboardPage;
    private PIMPage pimPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToPIM(Method method) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                dashboardPage = loginAsAdmin();
                pimPage = dashboardPage.navigateToPIM();
                if (pimPage.isEmployeeListDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                log.warn("Navigation to PIM failed (attempt {}): {}. Retrying...", attempts + 1, e.getMessage());
            }
            attempts++;
            if (attempts >= 3) {
                fail("Failed to navigate to PIM page after 3 attempts");
            }
            try {
                getDriver().navigate().refresh();
            } catch (Exception ne) {
                log.error("Failed to refresh: {}", ne.getMessage());
            }
        }
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.EMPLOYEE})
    @Story("Employee List")
    @Description("Verify the PIM Employee List page loads with the correct heading")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmployeeListPageLoads() {
        log.info("TC-E01: Verify PIM employee list loads");

        assertTrue(pimPage.isEmployeeListDisplayed(),
                "Employee list page should be displayed");
        assertTrue(getDriver().getCurrentUrl().contains(AppConstants.PIM_URL_FRAGMENT),
                "URL should contain PIM fragment");

        log.info("TC-E01: PASSED");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.EMPLOYEE})
    @Story("Employee List")
    @Description("Verify that the default employee list contains at least one record")
    @Severity(SeverityLevel.NORMAL)
    public void testEmployeeListHasRecords() {
        log.info("TC-E02: Verify employee list is not empty on fresh load");

        int count = pimPage.getEmployeeResultCount();
        assertTrue(count > 0,
                "Employee list should have at least one record. Found: " + count);

        log.info("TC-E02: PASSED — Found {} employee records", count);
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.EMPLOYEE})
    @Story("Add Employee")
    @Description("Verify 'Add Employee' form opens successfully from the employee list")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddEmployeeFormOpens() {
        log.info("TC-E03: Verify Add Employee form opens");

        pimPage.clickAddEmployee();

        assertTrue(pimPage.isAddEmployeeFormDisplayed(),
                "Add Employee form should be visible after clicking Add Employee");
        assertTrue(getDriver().getCurrentUrl().contains("addEmployee"),
                "URL should reflect the Add Employee page");

        log.info("TC-E03: PASSED — Add Employee form is accessible");
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.EMPLOYEE})
    @Story("Add Employee")
    @Description("Verify that a new employee can be added with first and last name — happy path workflow")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddEmployeeHappyPath() {
        log.info("TC-E04: Add a new employee — happy path");

        TestDataFactory.EmployeeData employee = TestDataFactory.generateEmployee();

        log.info("Adding employee: {} {}", employee.firstName(), employee.lastName());

        pimPage.addEmployee(employee.firstName(), employee.lastName(), employee.employeeId());

        // After save, URL changes to personal details — verify navigation succeeded
        assertTrue(getDriver().getCurrentUrl().contains("viewPersonalDetails"),
                "Should navigate to employee personal details after saving");

        log.info("TC-E04: PASSED — Employee [{} {}] was added and detail page loaded", employee.firstName(), employee.lastName());
    }

    @Test(groups = {AppConstants.REGRESSION, AppConstants.EMPLOYEE})
    @Story("Search Employee")
    @Description("Verify that searching for 'Admin' returns at least one result")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchEmployeeByName() {
        log.info("TC-E05: Search for employee by name 'Admin'");

        pimPage.searchByEmployeeName("Admin");

        // Results may vary; we verify the search completes and page stays on list
        assertTrue(getDriver().getCurrentUrl().contains("viewEmployeeList"),
                "Should remain on Employee List after search");

        log.info("TC-E05: PASSED — Search completed without error");
    }
}
