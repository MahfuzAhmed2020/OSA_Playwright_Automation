package com.osa.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/checkout/Recorder_pl.feature",
    glue = "com.osa.steps",
    tags = "@checkout",
    plugin = {"pretty", "html:target/checkout-reports/index.html",
        "json:target/checkout-reports/cucumber.json", "com.osa.reports.ExtentCucumberPlugin"},
    monochrome = true)
public class RunCheckoutTest extends AbstractTestNGCucumberTests {
}
