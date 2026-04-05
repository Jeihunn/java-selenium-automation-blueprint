package io.github.jeihunn.driver.factory;

import org.openqa.selenium.WebDriver;

/** Creates WebDriver instances for a specific execution strategy. */
public interface DriverFactory {
  WebDriver create();
}
