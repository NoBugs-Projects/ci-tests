package com.example.teamcity.ui.listeners;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.steps.AdminSteps;
import com.example.teamcity.ui.annotations.UserSession;
import com.example.teamcity.ui.pages.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * TestNG listener that automatically handles user login when @UserSession annotation is present.
 * This listener will be called before each test method execution and will perform login
 * if the test method or its class is annotated with @UserSession.
 */
public class UserSessionListener implements IInvokedMethodListener {
    private static final Logger logger = LoggerFactory.getLogger(UserSessionListener.class);

    static {
        logger.info("UserSessionListener class loaded successfully");
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.info("UserSessionListener.beforeInvocation called for method: {}", method.getTestMethod().getMethodName());
        
        if (shouldLogin(method, testResult)) {
            logger.info("UserSession annotation found, proceeding with login setup");
            // Generate test data if annotation is present
            Object testInstance = testResult.getInstance();
            if (testInstance instanceof BaseTest baseTest) {
                logger.info("Test instance is BaseTest, generating test data");
                baseTest.testData = TestDataGenerator.generate();
                // Use AdminSteps to create the user
                AdminSteps.createUser(baseTest.testData.getUser());
                logger.info("Generated test data and created user for test: {}", testResult.getName());
            } else {
                logger.warn("Test instance is not BaseTest: {}", testInstance.getClass().getName());
            }
            performLogin(testResult);
        } else {
            logger.info("No UserSession annotation found for method: {}", method.getTestMethod().getMethodName());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.info("UserSessionListener.afterInvocation called for method: {}", method.getTestMethod().getMethodName());
        // Optional: Add logout logic here if needed
    }

    private boolean shouldLogin(IInvokedMethod method, ITestResult testResult) {
        // Check if method or class has @UserSession annotation
        boolean methodHasAnnotation = method.getTestMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(UserSession.class);
        boolean classHasAnnotation = testResult.getTestClass().getRealClass().isAnnotationPresent(UserSession.class);
        
        logger.info("Checking annotations - Method has @UserSession: {}, Class has @UserSession: {}", 
                   methodHasAnnotation, classHasAnnotation);
        
        return methodHasAnnotation || classHasAnnotation;
    }

    private void performLogin(ITestResult testResult) {
        Object testInstance = testResult.getInstance();
        if (testInstance instanceof BaseTest baseTest) {
            User user = baseTest.testData.getUser();
            logger.info("Performing login for user: {}", user.getUsername());
            LoginPage.open().login(user);
            logger.info("Logged in as user: {}", user.getUsername());
        } else {
            logger.warn("Cannot perform login - test instance is not BaseTest: {}", testInstance.getClass().getName());
        }
    }
} 