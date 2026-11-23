@login
Feature: Mobile User login

  @positive @regression
  Scenario: Valid credentials open the authenticated shop
    When user signs in with configured credentials
    Then user is signed in to the shop

  @negative @regression
  Scenario: Invalid credentials show an error
    When user performs login actions
      | BOD | username    | a@gmail.com |
      | BOD | password    | Test@123    |
      | BOD | loginButton | click       |
    Then the login is rejected with a visible message
