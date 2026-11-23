package com.osa.steps;

import com.osa.factory.BrowserFactory;
import com.osa.pages.LoginPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.regex.Pattern;

public class LocalLoginSteps {
    @When("user signs in with configured credentials")
    public void login() {
        LoginPage login = new LoginPage(BrowserFactory.getPage());
        login.performAction("username", required("LOGIN_EMAIL"));
        login.performAction("password", required("LOGIN_PASSWORD"));
        login.performAction("loginButton", "click");
    }

    private String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank())
            throw new IllegalStateException("Set " + name + " before running tests");
        return value;
    }

    @Then("user is signed in to the shop")
    public void signedIn() {
        assertThat(BrowserFactory.getPage()).hasURL(Pattern.compile(".*/shop(?:\\?.*)?"));
        assertThat(BrowserFactory.getPage().locator("[data-signout]")).isVisible();
    }

    @Then("the login is rejected with a visible message")
    public void rejected() {
        assertThat(BrowserFactory.getPage().locator("#authNotice")).isVisible();
        assertThat(BrowserFactory.getPage().locator("#authNotice")).containsText(Pattern.compile("\\S+"));
        assertThat(BrowserFactory.getPage()).hasURL(Pattern.compile(".*/login(?:\\?.*)?"));
    }
}
