Feature: Simple checkout using the Recorder_pl XPath flow

  Background:
    Given the customer signs in for checkout with configured credentials
    When the customer adds one product to the current cart
    And the customer opens checkout from the cart
    And the customer selects a saved address, delivery service, and payment card

  @checkout
  Scenario: Place an order from the current cart
    When the customer places the order
    Then the checkout order confirmation is displayed

  @checkout-preview
  Scenario: Review checkout without submitting an order
    Then the checkout is ready to place an order
