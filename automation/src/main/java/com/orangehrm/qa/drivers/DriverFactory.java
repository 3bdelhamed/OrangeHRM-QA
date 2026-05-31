package com.orangehrm.qa.drivers;

import com.orangehrm.qa.config.ConfigReader;
import com.orangehrm.qa.constants.AppConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory manages WebDriver lifecycle using ThreadLocal storage,
 * enabling safe parallel test execution without shared state.
 *
 * <p>Usage pattern:
 * <pre>
 *   DriverFactory.initDriver();
 *   WebDriver driver = DriverFactory.getDriver();
 *   DriverFactory.quitDriver();
 * </pre>
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    /** ThreadLocal ensures each thread (parallel test) gets its own WebDriver instance. */
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
        throw new UnsupportedOperationException("DriverFactory is a utility class.");
    }

    /**
     * Initializes a WebDriver instance for the current thread based on config.
     * Should be called once per test method/class in @BeforeMethod.
     */
    public static void initDriver() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getBrowser().toLowerCase().trim();
        boolean headless = config.isHeadless();

        log.info("Initializing WebDriver — browser: [{}], headless: [{}]", browser, headless);

        WebDriver driver = switch (browser) {
            case AppConstants.CHROME  -> createChromeDriver(headless);
            case AppConstants.FIREFOX -> createFirefoxDriver(headless);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: '" + browser + "'. Supported: chrome, firefox"
            );
        };

        configureBrowser(driver, config);
        driverThreadLocal.set(driver);

        log.info("WebDriver initialized successfully for thread: [{}]", Thread.currentThread().getName());
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @throws IllegalStateException if initDriver() was not called first
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for thread: " + Thread.currentThread().getName() +
                    ". Call DriverFactory.initDriver() before using getDriver()."
            );
        }
        return driver;
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal storage.
     * Must be called in @AfterMethod to prevent resource leaks.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver for thread: [{}]", Thread.currentThread().getName());
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    // ── Private factory methods ───────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--disable-popup-blocking",
                "--disable-notifications",
                "--remote-allow-origins=*"
        );

        log.debug("ChromeOptions configured — headless: {}", headless);
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--width=1920", "--height=1080");

        log.debug("FirefoxOptions configured — headless: {}", headless);
        return new FirefoxDriver(options);
    }

    private static void configureBrowser(WebDriver driver, ConfigReader config) {
        log.info("Configuring browser — setting window size to 1920x1080, clearing cookies, setting page load timeout: {}s", config.getPageLoadTimeout());
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(config.getPageLoadTimeout())
        );
    }
}
