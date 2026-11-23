//mvn compile exec:java '-Dexec.mainClass=com.osa.pages.Without_Any_Format'
package com.osa.pages;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/** Standalone login example: page-object locators, actions, and launcher in one class. */
public class Without_Any_Format {
    private final Page page;
    private final Locator email;
    private final Locator password;
    private final Locator signInButton;
    private final Locator signOutButton;

    public Without_Any_Format(Page page) {
        this.page = page;
        email = page.locator("#email");
        password = page.locator("#password");
        signInButton = page.locator("#authForm button[type='submit']");
        signOutButton = page.locator("[data-signout]");
    }

    public void openLoginPage() {
        page.navigate("http://localhost:8087/login");
    }

    public void login(String emailAddress, String loginPassword) {
        email.fill(emailAddress);
        password.fill(loginPassword);
        signInButton.click();
    }

    public void verifySuccessfulLogin() {
        assertThat(page).hasURL(Pattern.compile("http://localhost:8087/shop(?:\\?.*)?"));
        assertThat(signOutButton).isVisible();
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set the " + name + " environment variable before running.");
        }
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        String emailAddress = requiredEnvironment("LOGIN_EMAIL");
        String loginPassword = requiredEnvironment("LOGIN_PASSWORD");

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setChannel("chrome").setHeadless(false));
             BrowserContext context = browser.newContext()) {
            Without_Any_Format loginPage = new Without_Any_Format(context.newPage());
            loginPage.openLoginPage();
            loginPage.login(emailAddress, loginPassword);
            loginPage.verifySuccessfulLogin();
            System.out.println("Login passed in Chrome.");

            if (Boolean.parseBoolean(System.getProperty("keep.browsers.open", "true"))) {
                System.out.println("Chrome remains open. Press Ctrl+C in the terminal to exit.");
                new CountDownLatch(1).await();
            }
        }
    }
}
