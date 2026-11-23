package com.osa.steps;
import com.osa.api.UsersValues;
import com.osa.factory.BrowserFactory;
import com.osa.pages.LoginPage;
import com.osa.utility.AppContains;
import com.osa.utility.DataTableHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import java.util.HashMap;
import java.util.List;
public class LoginSteps {
    private final DataTableHelper helper = new DataTableHelper();
    @When("user performs login actions")
    public void performLogin(DataTable data) {
        LoginPage page = new LoginPage(BrowserFactory.getPage());
        HashMap<String, List<UsersValues>> userInput = helper.verifyResponse(data);
        List<UsersValues> body = userInput.get("BOD");
        for (int i = 0; i < body.size(); i++) {
            UsersValues user = body.get(i);
            page.performAction(user.getField(), user.getExpectedValue());
            AppContains.LOG.info("Performed BOD action: {}", user.getField());
        }
    }
    @Then("user validates login response")
    public void validateLogin(DataTable data) {
        LoginPage page = new LoginPage(BrowserFactory.getPage());
        HashMap<String, List<UsersValues>> userInput = helper.verifyResponse(data);
        List<UsersValues> validation = userInput.get("VAL");
        for (int i = 0; i < validation.size(); i++) {
            UsersValues user = validation.get(i);
            Assert.assertEquals(page.actualValue(user.getField()), user.getExpectedValue(),
                "Mismatch for field: " + user.getField());
            AppContains.LOG.info("Validated VAL field: {}", user.getField());
        }
    }
}
