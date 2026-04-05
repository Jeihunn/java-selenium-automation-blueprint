package io.github.jeihunn.ui.pages;

import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import org.openqa.selenium.By;

/** Login page actions and validation helpers. */
public class LoginPage extends BasePage {

  private static final By USERNAME_INPUT = By.id("user-name");
  private static final By PASSWORD_INPUT = By.id("password");
  private static final By LOGIN_BUTTON = By.id("login-button");
  private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

  /** Creates the login page with the shared driver instance. */
  public LoginPage(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Opens the login page and waits until the form is ready. */
  public void open() {
    driver.get(ConfigurationManager.config().baseUrl());
    waitForPageLoad();
    waitForVisible(USERNAME_INPUT);
  }

  /** Enters the username value. */
  public void setUsername(String username) {
    type(USERNAME_INPUT, username);
  }

  /** Enters the password value. */
  public void setPassword(String password) {
    type(PASSWORD_INPUT, password);
  }

  /** Clicks the login button. */
  public void clickLogin() {
    click(LOGIN_BUTTON);
  }

  /** Performs a complete login using username and password. */
  public void login(String username, String password) {
    setUsername(username);
    setPassword(password);
    clickLogin();
  }

  /** Returns the login error message text. */
  public String getErrorMessage() {
    return getText(ERROR_MESSAGE);
  }
}
