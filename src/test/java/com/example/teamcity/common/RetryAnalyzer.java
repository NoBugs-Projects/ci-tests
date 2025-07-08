package com.example.teamcity.common;

import com.example.teamcity.api.config.EnvironmentConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for handling flaky tests.
 * Best practice: Implement retry logic for UI tests that may be flaky.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = Integer.parseInt(
            EnvironmentConfig.getProperty("retryCount", "3")
        );
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("Retrying test: " + result.getName() + 
                             " (Attempt " + retryCount + "/" + maxRetryCount + ")");
            return true;
        }
        return false;
    }
    
    /**
     * Reset retry count for new test
     */
    public void reset() {
        retryCount = 0;
    }
} 