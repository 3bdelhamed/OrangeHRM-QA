package com.orangehrm.qa.pages;

import com.orangehrm.qa.constants.AppConstants;
import com.orangehrm.qa.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for the PIM (Personnel Information Management) module.
 * Covers: Employee List, Add Employee, Search, and view operations.
 */
public class PIMPage extends BasePage {

    // ── Add Employee form locators ────────────────────────────────────────────

    @FindBy(xpath = "//nav[@class='oxd-topbar-body-nav']//a[text()='Add Employee']")
    private WebElement addEmployeeButton;

    @FindBy(css = "input[name='firstName']")
    private WebElement firstNameInput;

    @FindBy(css = "input[name='middleName']")
    private WebElement middleNameInput;

    @FindBy(css = "input[name='lastName']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Employee Id']]//input")
    private WebElement employeeIdInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    // ── Employee list / search locators ───────────────────────────────────────

    @FindBy(css = "h5.oxd-text--h5")
    private WebElement pageHeading;

    @FindBy(css = ".oxd-table-row--clickable")
    private List<WebElement> employeeRows;

    @FindBy(css = ".oxd-table-card")
    private List<WebElement> resultCards;

    @FindBy(xpath = "//div[contains(@class,'oxd-input-group')][.//label[text()='Employee Name']]//input")
    private WebElement searchEmployeeNameInput;

    @FindBy(css = "button[type='submit'].oxd-button--secondary")
    private WebElement searchButton;

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private List<WebElement> tableRows;

    // ── Actions ───────────────────────────────────────────────────────────────

    public PIMPage clickAddEmployee() {
        log.info("Clicking 'Add Employee' button");
        click(addEmployeeButton);
        waitForUrlContains("addEmployee");
        WaitUtil.waitForVisibility(By.cssSelector("input[name='firstName']"));
        return this;
    }

    public PIMPage enterFirstName(String firstName) {
        log.debug("Entering first name: {}", firstName);
        type(firstNameInput, firstName);
        return this;
    }

    public PIMPage enterLastName(String lastName) {
        log.debug("Entering last name: {}", lastName);
        type(lastNameInput, lastName);
        return this;
    }

    public PIMPage enterMiddleName(String middleName) {
        log.debug("Entering middle name: {}", middleName);
        type(middleNameInput, middleName);
        return this;
    }

    public PIMPage clearAndEnterEmployeeId(String employeeId) {
        log.debug("Setting employee ID: {}", employeeId);
        clearAndType(employeeIdInput, employeeId);
        return this;
    }

    /**
     * Completes the Add Employee form and saves. Returns this page (employee detail view).
     */
    public PIMPage saveEmployee() {
        log.info("Saving new employee record");
        click(saveButton);
        WaitUtil.waitForUrlContains("viewPersonalDetails");
        return this;
    }

    /**
     * Full workflow: open form → fill with custom ID → save.
     */
    public PIMPage addEmployee(String firstName, String lastName, String employeeId) {
        clickAddEmployee();
        enterFirstName(firstName);
        enterLastName(lastName);
        clearAndEnterEmployeeId(employeeId);
        saveEmployee();
        log.info("Employee [{} {}] with ID [{}] added successfully", firstName, lastName, employeeId);
        return this;
    }

    public PIMPage searchByEmployeeName(String name) {
        log.info("Searching for employee by name: {}", name);
        type(searchEmployeeNameInput, name);
        click(searchButton);
        return this;
    }

    // ── State queries ─────────────────────────────────────────────────────────

    public String getPageHeading() {
        return getText(pageHeading);
    }

    public int getEmployeeResultCount() {
        try {
            WaitUtil.getWait(5).until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.cssSelector(".oxd-table-body .oxd-table-row")));
        } catch (Exception e) {
            // Ignore
        }
        return tableRows.size();
    }

    public boolean isEmployeeListDisplayed() {
        try {
            waitForUrlContains(AppConstants.PIM_URL_FRAGMENT);
            waitForVisibility(pageHeading);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddEmployeeFormDisplayed() {
        try {
            waitForVisibility(firstNameInput);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasSearchResults() {
        return getEmployeeResultCount() > 0;
    }
}
