package com.osa.steps;

import com.osa.factory.BrowserFactory;
import com.osa.pages.CheckoutPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class CheckoutSteps {
    private CheckoutPage checkout;

    @Given("the customer signs in for checkout with configured credentials")
    public void login() {
        checkout = new CheckoutPage(BrowserFactory.getPage());
        checkout.login(requiredEnvironment("LOGIN_EMAIL"), requiredEnvironment("LOGIN_PASSWORD"));
    }

    @When("the customer adds one product to the current cart")
    public void addProduct() { checkout.addOneProduct(); }

    @When("the customer opens checkout from the cart")
    public void openCheckout() { checkout.openCheckout(); }

    @When("the customer selects a saved address, delivery service, and payment card")
    public void selectDetails() { checkout.selectCheckoutDetails(); }

    @When("the customer places the order")
    public void placeOrder() { checkout.placeOrder(); }

    @Then("the checkout order confirmation is displayed")
    public void confirmed() { checkout.verifyOrderConfirmation(); }

    @Then("the checkout is ready to place an order")
    public void ready() { checkout.verifyReadyToOrder(); }

    private String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Set " + name + " before checkout.");
        return value;
    }
}
