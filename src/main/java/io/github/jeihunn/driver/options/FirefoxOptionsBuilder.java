package io.github.jeihunn.driver.options;

import io.github.jeihunn.driver.config.DriverConfig;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Builds Firefox options from runtime driver configuration. */
public final class FirefoxOptionsBuilder implements OptionsBuilder<FirefoxOptions> {

  @Override
  public FirefoxOptions build(DriverConfig config) {

    FirefoxOptions options = new FirefoxOptions();

    if (config.headless()) {
      options.addArguments("-headless");
      options.addArguments("--width=1920");
      options.addArguments("--height=1080");
    }

    // Firefox controls notifications through preferences.
    if (config.disableNotifications()) {
      options.addPreference("dom.webnotifications.enabled", false);
    }

    return options;
  }
}
