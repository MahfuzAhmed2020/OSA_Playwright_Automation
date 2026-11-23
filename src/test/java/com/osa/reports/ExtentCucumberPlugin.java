package com.osa.reports;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import java.util.concurrent.ConcurrentHashMap;
/** Single JVM-wide report; event handlers synchronized for parallel runs. */
public class ExtentCucumberPlugin implements ConcurrentEventListener {
    private static final ExtentReports REPORT = new ExtentReports();
    private static final ConcurrentHashMap<String, ExtentTest> TESTS = new ConcurrentHashMap<>();
    static {
        REPORT.attachReporter(new ExtentSparkReporter("target/extent-reports/index.html"));
        REPORT.setSystemInfo("Framework", "Playwright Java + Cucumber + TestNG");
    }
    @Override public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::started);
        publisher.registerHandlerFor(TestCaseFinished.class, this::finished);
        publisher.registerHandlerFor(TestRunFinished.class, e -> { synchronized (REPORT) { REPORT.flush(); } });
    }
    private void started(TestCaseStarted e) {
        synchronized (REPORT) {
            TESTS.put(e.getTestCase().getId().toString(), REPORT.createTest(e.getTestCase().getName()));
        }
    }
    private void finished(TestCaseFinished e) {
        ExtentTest test = TESTS.remove(e.getTestCase().getId().toString());
        if (test == null) return;
        synchronized (REPORT) {
            if (e.getResult().getStatus() == io.cucumber.plugin.event.Status.PASSED) test.pass("Passed");
            else if (e.getResult().getStatus() == io.cucumber.plugin.event.Status.SKIPPED) test.skip("Skipped");
            else if (e.getResult().getError() == null) test.fail("Failed");
            else test.fail(e.getResult().getError());
        }
    }
}
