package com.osa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/** XPath page object for the checkout flow recorded in Recorder_pl. */
public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void login(String email, String password) {
        page.locator("xpath=//input[@id='email']").fill(email);
        page.locator("xpath=//input[@id='password']").fill(password);
        page.locator("xpath=//form[@id='authForm']//button[@type='submit']").click();
        assertThat(page).hasURL(Pattern.compile(".*/shop(?:\\?.*)?"));
        assertThat(page.locator("xpath=//*[@data-signout]")).isVisible();
    }

    public void addOneProduct() {
        Response response = page.waitForResponse(result -> result.url().endsWith("/api/cart")
                && result.request().method().equals("POST"),
                () -> page.locator("xpath=(//button[@data-add])[1]").click());
        if (!response.ok()) throw new IllegalStateException("Add to cart failed: HTTP " + response.status());
        assertThat(page.locator("xpath=//*[@id='globalNotice']")).containsText("Added to your cart.");
    }

    public void openCheckout() {
        page.locator("xpath=(//a[@href='/cart'])[1]").click();
        assertThat(page).hasURL(Pattern.compile(".*/cart(?:\\?.*)?"));
        page.locator("xpath=//a[@id='checkoutLink']").click();
        assertThat(page).hasURL(Pattern.compile(".*/checkout(?:\\?.*)?"));
    }

    public void selectCheckoutDetails() {
        selectFirstSavedOption(page.locator("xpath=//select[@id='address']"));
        selectFirstSavedOption(page.locator("xpath=//select[@id='deliveryService']"));
        selectFirstSavedOption(page.locator("xpath=//select[@id='card']"));
    }

    public void verifyReadyToOrder() {
        assertThat(page.locator("xpath=//select[@id='address']")).not().hasValue("");
        assertThat(page.locator("xpath=//select[@id='deliveryService']")).not().hasValue("");
        assertThat(page.locator("xpath=//select[@id='card']")).not().hasValue("");
        assertThat(page.locator("xpath=//button[@id='Checkout']")).isEnabled();
    }

    public void placeOrder() {
        verifyReadyToOrder();
        page.locator("xpath=//button[@id='Checkout']").click();
    }

    public void verifyOrderConfirmation() {
        Locator confirmation = page.locator("xpath=//*[@id='orderSuccess']");
        assertThat(confirmation).isVisible();
        assertThat(confirmation).containsText("Thank you for your order.");
    }

    private void selectFirstSavedOption(Locator select) {
        assertThat(select).isEnabled();
        select.locator("xpath=./option[2]").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        select.selectOption(new SelectOption().setIndex(1));
    }
}
