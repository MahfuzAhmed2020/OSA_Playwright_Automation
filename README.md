# OSA Playwright Automation (Java)

Login automation for http://localhost:8087/login using Java, Playwright, TestNG, Cucumber, and the Page Object Model (POM).

## Requirements

- JDK 17 or newer.
- Maven 3.9 or newer.
- Google Chrome installed; Microsoft Edge installed for the parallel suite.
- The local application running at http://localhost:8087.
- Internet access for Maven dependencies and initial Playwright browser installation.

## Set up your terminal

Run commands in PowerShell from the project folder. Set these variables in each new terminal session:

```powershell
$env:LOGIN_EMAIL='a@gmail.com'
$env:LOGIN_PASSWORD='Test@123'
$env:PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD='1'
```

## Run Without_Any_Format.java

This standalone class contains the page locators, login actions, verification, and Chrome launcher together in one class. It uses the credentials set above.

```powershell
mvn compile exec:java '-Dexec.mainClass=com.osa.pages.Without_Any_Format'
```

It opens Chrome, signs in, and checks the shop URL and visible sign-out control. After `Login passed in Chrome.`, the browser stays open and the terminal remains active. Press **Ctrl+C** when finished inspecting.

To close Chrome automatically after verification:

```powershell
mvn compile exec:java '-Dexec.mainClass=com.osa.pages.Without_Any_Format' '-Dkeep.browsers.open=false'
```

In your IDE, run the `main` method in `src/main/java/com/osa/pages/Without_Any_Format.java`. Add `LOGIN_EMAIL`, `LOGIN_PASSWORD`, and `PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1` to the run configuration's environment variables. Use the IDE's Stop button to end inspection.

## Run login tests in Chrome

```powershell
mvn test '-Dheadless=false' '-Dsurefire.suiteXmlFiles=testng-local.xml'
```

This runs the Cucumber login scenarios in Chrome and closes each browser after its scenario. Set `-Dheadless=true` to hide browser windows.

## Run Chrome, Firefox, and Edge in parallel

Firefox requires Playwright's Firefox binary. Install it once if missing:

```powershell
$env:PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD='1'
mvn exec:java '-Dexec.mainClass=com.microsoft.playwright.CLI' '-Dexec.args=install firefox'
```

Start all three browsers in parallel:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-parallel.xml'
```

The suite uses three TestNG workers, forces visible browser windows, and keeps each scenario's browser open. With two scenarios per browser, up to six windows remain open. The terminal stays active after the tests finish; press **Ctrl+C** to exit inspection. Maven will not return a normal completion status while inspection mode is active.

For a run that closes browsers and returns an exit status:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-parallel.xml' '-Dkeep.browsers.open=false' '-Dheadless=true'
```

## Run only the successful-login scenario

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-parallel.xml' '-Dcucumber.filter.tags=@positive'
```

For Firefox only, with the browser kept open:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-firefox.xml' '-Dcucumber.filter.tags=@positive'
```

The negative scenario must use invalid credentials to test login rejection. Valid credentials will sign in and fail the rejection assertion.

## Default browser suite

`mvn test` uses `testng.xml`, which runs Chromium, Firefox, and WebKit in parallel. Install those bundled browsers first:

```powershell
mvn exec:java '-Dexec.mainClass=com.microsoft.playwright.CLI' '-Dexec.args=install chromium firefox webkit'
mvn test '-Dheadless=true'
```

## Reports

TestNG/Cucumber runs generate:

| Output | Path |
| --- | --- |
| Cucumber HTML | `target/cucumber-reports/index.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| Extent HTML | `target/extent-reports/index.html` |
| TestNG/Surefire results | `target/surefire-reports/` |
| Application logs | `target/logs/automation.log` |
| Failure screenshots | `target/screenshots/` |

Parallel Cucumber runners share the HTML/JSON report paths, so these reports may overwrite one another. The standalone Java class prints its result to the terminal and does not generate Cucumber or TestNG reports.

## Project structure

- `src/main/java/com/osa/pages/`: page objects and the standalone `Without_Any_Format` launcher.
- `src/main/java/com/osa/factory/`: browser setup and lifecycle management.
- `src/main/java/com/osa/utility/`: configuration, logging, and screenshots.
- `src/test/java/com/osa/steps/`: Cucumber steps and scenario hooks.
- `src/test/java/com/osa/utility/`: DataTable and assertion/JSON helpers.
- `src/test/resources/features/`: Cucumber scenarios using BOD action rows and VAL validation rows.
- `src/main/resources/config.properties`: default login URL, headless setting, viewport, and screenshot path.

The Cucumber suites read the login URL from `qa.url`; the standalone class defines it in `openLoginPage()`.

## Recorder_pl checkout with BDD Cucumber

Set `LOGIN_EMAIL`, `LOGIN_PASSWORD`, and `PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD` as above. The dedicated checkout suite uses Chrome and keeps the browser open.

To place an order from the current cart:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-checkout.xml'
```

To check the flow up to an enabled Place order button without submitting:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-checkout.xml' '-Dcucumber.filter.tags=@checkout-preview'
```

Each scenario adds one product to the existing cart and selects the first saved address, available delivery quote, and saved payment card. Existing cart items are included if you submit. Press Ctrl+C after inspection.

- Feature: `src/test/resources/checkout/Recorder_pl.feature`
- Step definitions: `src/test/java/com/osa/steps/CheckoutSteps.java`
- XPath page object: `src/main/java/com/osa/pages/CheckoutPage.java`
- Runner: `src/test/java/com/osa/runners/RunCheckoutTest.java`
- Cucumber report: `target/checkout-reports/index.html`

The checkout feature is separate from the login feature directory so the existing login suites do not submit checkout orders.
