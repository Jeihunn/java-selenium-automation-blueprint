package io.github.jeihunn.driver.options;

import io.github.jeihunn.driver.config.DriverConfig;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeOptions;

/** Builds Chrome options from runtime driver configuration. */
public final class ChromeOptionsBuilder implements OptionsBuilder<ChromeOptions> {

  @Override
  public ChromeOptions build(DriverConfig config) {

    ChromeOptions options = new ChromeOptions();

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

    options.addArguments("--disable-save-password-bubble");
    options.addArguments("--disable-password-manager-reauthentication");

    // Disable password manager prompts that can interrupt automation flows.
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("credentials_enable_service", false);
    prefs.put("profile.password_manager_enabled", false);
    prefs.put("profile.password_manager_leak_detection", false);

    options.setExperimentalOption("prefs", prefs);

    return options;
  }
}
