package com.osa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** Playwright POM: locators stay here, logical names come from BDD tables. */
public class LoginPage {
    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator errorMessage;
    private final Locator title;

    public LoginPage(Page page) {
        this.page = page;
        username = page.locator("#email");
        password = page.locator("#password");
        loginButton = page.locator("#authForm button[type=submit]");
        errorMessage = page.locator("#authNotice");
        title = page.locator("h1");
    }

    public void performAction(String field, String value) {
        if (field.equalsIgnoreCase("username"))
            username.fill(value);
        else if (field.equalsIgnoreCase("password"))
            password.fill(value);
        else if (field.equalsIgnoreCase("loginButton") && value.equalsIgnoreCase("click"))
            loginButton.click();
        else
            throw new IllegalArgumentException("Unsupported BOD action: " + field + " = " + value);
    }

    public String actualValue(String field) {
        if (field.equalsIgnoreCase("username"))
            return username.inputValue();
        if (field.equalsIgnoreCase("password"))
            return password.inputValue();
        if (field.equalsIgnoreCase("errorMessage"))
            return errorMessage.innerText();
        if (field.equalsIgnoreCase("title"))
            return title.innerText();
        if (field.equalsIgnoreCase("pageTitle"))
            return page.title();
        if (field.equalsIgnoreCase("url"))
            return page.url();
        throw new IllegalArgumentException("Unsupported VAL field: " + field);
    }
}

