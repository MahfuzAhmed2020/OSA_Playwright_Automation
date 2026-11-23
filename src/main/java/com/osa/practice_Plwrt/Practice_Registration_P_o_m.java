package com.osa.practice_Plwrt;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Registration page object: named elements and actions, like a PageFactory
 * page.
 */
public class Practice_Registration_P_o_m {

    private static final Logger log = LogManager.getLogger(Practice_Registration_P_o_m.class);
    private final Page page;
    private final String registrationUrl = System.getProperty("app.base.url", "http://localhost:8087")
            .replaceAll("/+$", "") + "/register";

    private final Locator firstName;
    private final Locator lastName;
    private final Locator email;
    private final Locator password;
    private final Locator confirmPassword;
    private final Locator termsAndPrivacy;
    private final Locator createAccount;
    private final Locator message;
    private final Locator signIn;
    private final Locator forgotPassword;

    public Practice_Registration_P_o_m(Page page) {
        this.page = page;
        // Playwright Locators are lazy and automatically wait when used.
        firstName = page.locator("#first_name");
        lastName = page.locator("#last_name");
        email = page.locator("#email");
        password = page.locator("#password");
        confirmPassword = page.locator("#confirm_password");
        termsAndPrivacy = page.locator("#authForm input[type='checkbox']");
        createAccount = page.locator("#authForm button[type='submit']");
        message = page.locator("#authNotice");
        signIn = page.locator("#main a[href='/login']");
        forgotPassword = page.locator("#main a[href='/forgot-password']");
    }

    public void open() {
        page.navigate(registrationUrl);
        log.info("==== Opened registration page ====");
    }

    public void enterFirstName(String value) {
        firstName.fill(value);
        assertThat(firstName).hasValue(value);
        log.info("==== Entered first name ====");
    }

    public void enterLastName(String value) {
        lastName.fill(value);
        assertThat(lastName).hasValue(value);
        log.info("==== Entered last name ====");
    }

    public void enterEmail(String value) {
        email.fill(value);
        assertThat(email).hasValue(value);
        log.info("==== Entered email ====");
    }

    public void enterPassword(String value) {
        password.fill(value);
        assertThat(password).hasValue(value);
        log.info("==== Entered password ====");
    }

    public void enterConfirmPassword(String value) {
        confirmPassword.fill(value);
        assertThat(confirmPassword).hasValue(value);
        log.info("==== Entered confirm password ====");
    }

    public void acceptTermsAndPrivacy() {
        termsAndPrivacy.check();
        assertThat(termsAndPrivacy).isChecked();
        log.info("==== Accepted terms and privacy policy ====");
    }

    public void clickCreateAccountButton() {
        assertThat(createAccount).isEnabled();
        createAccount.click();
        log.info("==== Clicked Create account button ====");
    }

    public String getMessage() {
        message.waitFor();
        assertThat(message).not().hasText("");
        String text = message.innerText();
        log.info("==== Read registration message: {} ====", text);
        return text;
    }

    public void verifyPageLoaded() {
        assertThat(page).hasURL(registrationUrl);
        assertThat(page).hasTitle("Register | Mobile User");
        assertThat(page.locator("h1")).hasText("Create account");
        assertThat(firstName).isVisible();
        assertThat(lastName).isVisible();
        assertThat(email).isVisible();
        assertThat(password).isVisible();
        assertThat(confirmPassword).isVisible();
        assertThat(termsAndPrivacy).isVisible();
        assertThat(createAccount).isVisible();
        log.info("==== Verified registration page loaded ====");
    }

    public void verifyMessage(String expectedMessage) {
        assertThat(message).isVisible();
        assertThat(message).hasText(expectedMessage);
        log.info("==== Verified registration message ====");
    }

    public void clickSignIn() {
        signIn.click();
        log.info("==== Clicked Sign in link ====");
    }

    public void clickForgotPassword() {
        forgotPassword.click();
        log.info("==== Clicked Forgot password link ====");
    }
}
