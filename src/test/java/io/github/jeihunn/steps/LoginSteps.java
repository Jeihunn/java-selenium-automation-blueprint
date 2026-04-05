package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jeihunn.data.TestDataRegistry;
import io.github.jeihunn.data.model.User;
import io.github.jeihunn.ui.pages.LoginPage;

/** Step definitions for login actions and validations. */
public class LoginSteps {

  private final LoginPage loginPage;
  private final TestDataRegistry testDataRegistry;

  /** Creates login steps with required collaborators. */
  public LoginSteps(LoginPage loginPage, TestDataRegistry testDataRegistry) {
    this.loginPage = loginPage;
    this.testDataRegistry = testDataRegistry;
  }

  @Given("the user is on the login page")
  public void navigateToLoginPage() {
    loginPage.open();
  }

  @When("the user enters {string} as the username")
  public void enterUsername(String value) {

    String username = testDataRegistry.getUser(value).map(User::username).orElse(value);

    loginPage.setUsername(username);
  }

  @When("the user enters {string} as the password")
  public void enterPassword(String value) {

    String password = testDataRegistry.getUser(value).map(User::password).orElse(value);

    loginPage.setPassword(password);
  }

  @When("the user clicks the Login button")
  public void clickLoginButton() {
    loginPage.clickLogin();
  }

  @Then("the login page should display the error message {string}")
  public void verifyErrorMessage(String expectedMessage) {
    assertThat(loginPage.getErrorMessage())
        .as("Login page error message is incorrect")
        .isEqualTo(expectedMessage);
  }
}
