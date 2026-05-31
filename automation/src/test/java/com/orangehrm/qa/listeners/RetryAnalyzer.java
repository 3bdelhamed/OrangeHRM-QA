package com.orangehrm.qa.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer: automatically retries flaky tests up to MAX_RETRY_COUNT times
 * before marking them as failed.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 1;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying test [{}] — attempt {}/{}",
                    result.getMethod().getMethodName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
