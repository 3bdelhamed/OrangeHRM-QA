package com.orangehrm.qa.utils;

import com.orangehrm.qa.config.ConfigReader;
import com.orangehrm.qa.drivers.DriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utility that:
 * 1. Captures screenshots to disk for archiving
 * 2. Attaches them inline to the Allure HTML report
 */
public final class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {}

    /**
     * Captures a screenshot, saves it to disk, and attaches it to the current Allure test step.
     *
     * @param testName Descriptive name used in the filename (test method name)
     * @return absolute path of the saved screenshot, or null if capture failed
     */
    public static String captureAndAttach(String testName) {
        if (!ConfigReader.getInstance().isScreenshotOnFailure()) {
            log.debug("Screenshot on failure is disabled in config — skipping.");
            return null;
        }

        WebDriver driver;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            log.warn("WebDriver not available — cannot capture screenshot.");
            return null;
        }

        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // Attach to Allure report (inline in HTML)
            Allure.addAttachment(
                    "Screenshot on Failure: " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    "png"
            );

            // Also save to disk for manual inspection
            String filePath = saveScreenshotToDisk(screenshotBytes, testName);
            log.info("Screenshot captured and attached to Allure report: {}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("Failed to capture screenshot for test '{}': {}", testName, e.getMessage());
            return null;
        }
    }

    /**
     * Saves raw screenshot bytes to the configured directory.
     */
    private static String saveScreenshotToDisk(byte[] bytes, String testName) throws IOException {
        String screenshotDir = ConfigReader.getInstance().getScreenshotDir();
        Path dirPath = Paths.get(screenshotDir);
        Files.createDirectories(dirPath);

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String sanitizedName = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String fileName = sanitizedName + "_" + timestamp + ".png";
        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, bytes);
        return filePath.toAbsolutePath().toString();
    }

    /**
     * Takes a named screenshot for a specific step (not just on failure).
     * Useful for documenting workflow steps in reports.
     */
    public static void captureStep(String stepDescription) {
        try {
            byte[] bytes = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(
                    stepDescription,
                    "image/png",
                    new ByteArrayInputStream(bytes),
                    "png"
            );
            log.debug("Step screenshot captured: {}", stepDescription);
        } catch (Exception e) {
            log.warn("Could not capture step screenshot '{}': {}", stepDescription, e.getMessage());
        }
    }
}
