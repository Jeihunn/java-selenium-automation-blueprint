package io.github.jeihunn.driver;

import org.openqa.selenium.WebDriver;

/** Provides WebDriver instances for dependency injection. */
public class DriverProvider {

  public WebDriver get() {
    return DriverManager.getDriver();
  }
}
