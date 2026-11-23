package com.osa.listeners;

import com.osa.utility.AppContains;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Cucumber hooks capture per-scenario screenshots; this listener logs TestNG
 * lifecycle.
 */
public class TestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        AppContains.LOG.info("TestNG context started: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        AppContains.LOG.info("START: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        AppContains.LOG.info("PASS: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        AppContains.LOG.error("FAIL: {}", result.getName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        AppContains.LOG.warn("SKIP: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        AppContains.LOG.info("TestNG context finished: {}", context.getName());
    }
}
