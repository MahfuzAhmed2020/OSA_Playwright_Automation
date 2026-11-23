package com.osa.pages;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/** One product added to the current cart, followed by a simple checkout. */
public class Recorder_pl {
    public static void main(String[] args) throws InterruptedException {
        String email = requiredEnvironment("LOGIN_EMAIL");
        String password = requiredEnvironment("LOGIN_PASSWORD");

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setChannel("chrome").setHeadless(false));
             BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            page.navigate("http://localhost:8087/login");
            page.locator("xpath=//input[@id='email']").fill(email);
            page.locator("xpath=//input[@id='password']").fill(password);
            page.locator("xpath=//form[@id='authForm']//button[@type='submit']").click();
            assertThat(page).hasURL("http://localhost:8087/shop");
            assertThat(page.locator("xpath=//*[@data-signout]")).isVisible();

            // Wait for the asynchronous cart update before navigating away.
            Response added = page.waitForResponse(response ->
                    response.url().endsWith("/api/cart") && response.request().method().equals("POST"),
                    () -> page.locator("xpath=(//button[@data-add])[1]").click());
            if (!added.ok()) throw new IllegalStateException("Add to cart failed: HTTP " + added.status());
            assertThat(page.locator("xpath=//*[@id='globalNotice']")).containsText("Added to your cart.");

            page.locator("xpath=(//a[@href='/cart'])[1]").click();
            assertThat(page).hasURL("http://localhost:8087/cart");
            page.locator("xpath=//a[@id='checkoutLink']").click();
            assertThat(page).hasURL(Pattern.compile("http://localhost:8087/checkout(?:\\?.*)?"));

            // Index 0 is a placeholder. Delivery quotes are loaded after address selection.
            selectFirstSavedOption(page.locator("xpath=//select[@id='address']"));
            selectFirstSavedOption(page.locator("xpath=//select[@id='deliveryService']"));
            selectFirstSavedOption(page.locator("xpath=//select[@id='card']"));

            Locator placeOrder = page.locator("xpath=//button[@id='Checkout']");
            assertThat(placeOrder).isEnabled();

            if (Boolean.parseBoolean(System.getProperty("checkout.submit", "true"))) {
                // Click once. Do not retry submission, which could create another order.
                placeOrder.click();
                assertThat(page.locator("xpath=//*[@id='orderSuccess']")).isVisible();
                assertThat(page.locator("xpath=//*[@id='orderSuccess']"))
                        .containsText("Thank you for your order.");
                System.out.println("Checkout passed: order confirmed.");
            } else {
                System.out.println("Checkout ready: address, delivery, and card selected. No order submitted.");
            }

            if (Boolean.parseBoolean(System.getProperty("keep.browsers.open", "true"))) {
                System.out.println("Chrome remains open. Press Ctrl+C in the terminal to exit.");
                new CountDownLatch(1).await();
            }
        }
    }

    private static void selectFirstSavedOption(Locator select) {
        assertThat(select).isEnabled();
        select.locator("xpath=./option[2]").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        select.selectOption(new SelectOption().setIndex(1));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set " + name + " before running checkout.");
        }
        return value;
    }
}
