package io.github.jeihunn.driver.config;

import io.github.jeihunn.config.Configuration;
import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.enums.BrowserType;
import io.github.jeihunn.driver.enums.ExecutionMode;

/** Immutable WebDriver runtime configuration. */
public record DriverConfig(
    ExecutionMode mode,
    BrowserType browser,
    boolean headless,
    boolean startMaximized,
    boolean disableNotifications,
    boolean disablePopupBlocking) {

  /** Creates a driver configuration from external properties. */
  public static DriverConfig fromConfig() {

    Configuration config = ConfigurationManager.config();

    return new DriverConfig(
        ExecutionMode.from(config.executionMode()),
        BrowserType.from(config.browser()),
        config.isHeadless(),
        config.isStartMaximized(),
        config.isDisableNotifications(),
        config.isDisablePopupBlocking());
  }
}
