package com.orangehrm.qa.listeners;

import com.orangehrm.qa.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * TestNG + Allure listener that:
 * - Captures and attaches screenshots automatically on test failure
 * - Enriches Allure reports with environment info and test metadata
 * - Logs test lifecycle events (start, pass, fail, skip)
 */
public class AllureListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(AllureListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("====== TEST SUITE STARTED: [{}] ======", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed  = context.getPassedTests().size();
        int failed  = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total   = passed + failed + skipped;

        log.info("====== TEST SUITE FINISHED: [{}] ======", context.getName());
        log.info("Results — Total: {} | Passed: {} | Failed: {} | Skipped: {}",
                total, passed, failed, skipped);
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("---> TEST STARTED: [{}]", getTestName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✓ TEST PASSED: [{}] — Duration: {}ms",
                getTestName(result), getDuration(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        log.error("✗ TEST FAILED: [{}] — Reason: {}",
                testName, result.getThrowable().getMessage());

        // Attach screenshot to Allure report
        ScreenshotUtil.captureAndAttach(testName);

        // Attach failure cause to Allure as text
        Allure.addAttachment(
                "Failure Cause",
                "text/plain",
                result.getThrowable().toString()
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⊘ TEST SKIPPED: [{}]", getTestName(result));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("⚠ TEST WITHIN SUCCESS THRESHOLD: [{}]", getTestName(result));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String getTestName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "#" + result.getMethod().getMethodName();
    }

    private long getDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
