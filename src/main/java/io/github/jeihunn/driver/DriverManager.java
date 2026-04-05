package io.github.jeihunn.driver;

import io.github.jeihunn.driver.config.DriverConfig;
import io.github.jeihunn.driver.factory.DriverFactory;
import io.github.jeihunn.driver.factory.DriverFactoryResolver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Manages one WebDriver instance per thread. */
public final class DriverManager {

  private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);

  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  private DriverManager() {}

  public static WebDriver getDriver() {
    WebDriver driver = DRIVER.get();

    if (driver == null) {

      DriverConfig config = DriverConfig.fromConfig();

      LOG.info(
          "[DRIVER-INIT] mode={} browser={} headless={} thread={}",
          config.mode(),
          config.browser(),
          config.headless(),
          Thread.currentThread().threadId());

      DriverFactory factory = DriverFactoryResolver.getFactory(config);

      driver = factory.create();
      DRIVER.set(driver);
    }

    return driver;
  }

  public static WebDriver getActiveDriver() {
    return DRIVER.get();
  }

  public static void quitDriver() {
    WebDriver driver = DRIVER.get();

    if (driver != null) {
      try {
        LOG.info("[DRIVER-QUIT] thread={}", Thread.currentThread().threadId());
        driver.quit();
      } finally {
        DRIVER.remove();
      }
    }
  }
}
