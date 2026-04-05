package io.github.jeihunn.driver.options;

import io.github.jeihunn.driver.config.DriverConfig;
import org.openqa.selenium.edge.EdgeOptions;

/** Builds Edge options from runtime driver configuration. */
public final class EdgeOptionsBuilder implements OptionsBuilder<EdgeOptions> {

  @Override
  public EdgeOptions build(DriverConfig config) {

    EdgeOptions options = new EdgeOptions();

    if (config.headless()) {
      options.addArguments("--headless=new");
      options.addArguments("--window-size=1920,1080");
    }

    if (config.startMaximized()) {
      options.addArguments("--start-maximized");
    }

    if (config.disableNotifications()) {
      options.addArguments("--disable-notifications");
    }

    if (config.disablePopupBlocking()) {
      options.addArguments("--disable-popup-blocking");
    }

    return options;
  }
}
