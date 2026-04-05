package io.github.jeihunn.driver.factory;

import io.github.jeihunn.driver.config.DriverConfig;
import io.github.jeihunn.driver.enums.BrowserType;
import io.github.jeihunn.driver.options.ChromeOptionsBuilder;
import io.github.jeihunn.driver.options.EdgeOptionsBuilder;
import io.github.jeihunn.driver.options.FirefoxOptionsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/** Creates local WebDriver instances. */
public final class LocalDriverFactory implements DriverFactory {

  private final DriverConfig config;

  public LocalDriverFactory(DriverConfig config) {
    this.config = config;
  }

  @Override
  public WebDriver create() {

    BrowserType browser = config.browser();

    return switch (browser) {
      case CHROME -> new ChromeDriver(new ChromeOptionsBuilder().build(config));
      case FIREFOX -> new FirefoxDriver(new FirefoxOptionsBuilder().build(config));
      case EDGE -> new EdgeDriver(new EdgeOptionsBuilder().build(config));
    };
  }
}
