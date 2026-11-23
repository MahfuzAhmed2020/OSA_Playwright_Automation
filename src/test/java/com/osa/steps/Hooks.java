package com.osa.steps;
import com.osa.factory.BrowserFactory;
import com.osa.utility.AppContains;
import com.osa.utility.PropertyReader;
import com.osa.utility.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.testng.Reporter;
public class Hooks {
    private boolean keepOpen;
    @Before
    public void before(Scenario scenario) {
        String browser = Reporter.getCurrentTestResult() == null ? null :
            Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
        if (browser == null || browser.isBlank()) browser = System.getProperty("browser", "chromium");
        String suiteKeepOpen = Reporter.getCurrentTestResult() == null ? "false" :
            Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("keep.browsers.open");
        keepOpen = Boolean.parseBoolean(System.getProperty("keep.browsers.open", suiteKeepOpen));
        BrowserFactory.initializeBrowser(browser, keepOpen);
        BrowserFactory.getPage().navigate(PropertyReader.getProperty("qa.url"));
        AppContains.LOG.info("Starting {} on {}", scenario.getName(), browser);
    }
    @After
    public void after(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                try {
                    byte[] image = ScreenshotUtil.captureBytes(BrowserFactory.getPage());
                    scenario.attach(image, "image/png", "Failure screenshot");
                    ScreenshotUtil.save(BrowserFactory.getPage(), scenario.getName());
                } catch (Exception e) { AppContains.LOG.error("Screenshot failed", e); }
            }
            AppContains.LOG.info("Finished {}: {}", scenario.getName(), scenario.getStatus());
        } finally {
            if (keepOpen) BrowserFactory.retainBrowser();
            else BrowserFactory.closeBrowser();
        }
    }
}
