package com.osa.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.osa.steps", 
    plugin = { "pretty",
        "html:target/cucumber-reports/index.html", "json:target/cucumber-reports/cucumber.json",
        "com.osa.reports.ExtentCucumberPlugin" }, 
    monochrome = true)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}
