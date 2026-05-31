package com.orangehrm.qa.base;

import com.orangehrm.qa.config.ConfigReader;
import com.orangehrm.qa.drivers.DriverFactory;
import com.orangehrm.qa.pages.LoginPage;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Abstract base for all test classes.
 *
 * Responsibilities:
 * - Initialize and tear down WebDriver per test method
 * - Navigate to the base URL before each test
 * - Provide shared helpers (getDriver, navigateToLoginPage)
 * - All test classes extend this; no test logic lives here
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());
    protected ConfigReader config = ConfigReader.getInstance();

    /**
     * Initializes WebDriver and navigates to the login page before each test.
     * @param method injected by TestNG — used to log which test is starting
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info("========================================");
        log.info("  STARTING TEST: {}", method.getName());
        log.info("========================================");

        int attempts = 0;
        while (attempts < 3) {
            try {
                DriverFactory.initDriver();
                DriverFactory.getDriver().get(config.getBaseUrl());
                log.info("Browser navigated to: {}", config.getBaseUrl());
                return;
            } catch (Exception e) {
                log.warn("Navigation to base URL failed (attempt {}): {}. Retrying...", attempts + 1, e.getMessage());
                attempts++;
                if (attempts >= 3) {
                    throw e;
                }
                try {
                    DriverFactory.quitDriver();
                } catch (Exception qe) {
                    log.error("Error quitting driver: {}", qe.getMessage());
                }
            }
        }
    }

    /**
     * Quits the WebDriver after each test, regardless of pass/fail.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(Method method) {
        log.info("----------------------------------------");
        log.info("  TEARDOWN: {}", method.getName());
        log.info("----------------------------------------");
        DriverFactory.quitDriver();
        try {
            // Pacing delay to let the shared public server breathe between sessions
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ── Protected helpers ─────────────────────────────────────────────────────

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    /**
     * Returns a LoginPage instance ready for interaction.
     * Call in @BeforeMethod or at the start of a test method.
     */
    @Step("Open Login Page")
    protected LoginPage openLoginPage() {
        return new LoginPage();
    }

    /**
     * Performs a standard admin login and returns the DashboardPage.
     * Shared by any test that needs an authenticated session as a precondition.
     */
    @Step("Login as Admin")
    protected com.orangehrm.qa.pages.DashboardPage loginAsAdmin() {
        log.info("Logging in as admin user");
        int attempts = 0;
        while (attempts < 3) {
            try {
                return openLoginPage().loginWithValidCredentials(
                        config.getAdminUsername(),
                        config.getAdminPassword()
                );
            } catch (Exception e) {
                log.warn("Login as admin failed (attempt {}): {}. Retrying...", attempts + 1, e.getMessage());
                attempts++;
                if (attempts >= 3) {
                    throw e;
                }
                try {
                    getDriver().navigate().refresh();
                } catch (Exception ne) {
                    log.error("Failed to refresh page: {}", ne.getMessage());
                }
            }
        }
        throw new RuntimeException("Failed to login as admin after 3 attempts");
    }
}
