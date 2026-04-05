package io.github.jeihunn.config;

import org.aeonbits.owner.Config;

/** Central configuration contract for the test framework. */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
  "system:properties",
  "system:env",
  "file:.env",
  "classpath:config-${env}.properties",
  "classpath:config.properties"
})
public interface Configuration extends Config {

  // Environment
  @Key("env")
  String env();

  @Key("base.url")
  String baseUrl();

  // Execution
  @Key("execution.mode")
  @DefaultValue("local")
  String executionMode();

  @Key("GRID_HOST")
  String gridHost();

  @Key("GRID_USERNAME")
  String gridUsername();

  @Key("GRID_PASSWORD")
  String gridPassword();

  // Browser
  @Key("browser")
  @DefaultValue("chrome")
  String browser();

  @Key("browser.headless")
  @DefaultValue("false")
  boolean isHeadless();

  @Key("browser.start.maximized")
  @DefaultValue("true")
  boolean isStartMaximized();

  @Key("browser.disable.notifications")
  @DefaultValue("true")
  boolean isDisableNotifications();

  @Key("browser.disable.popup.blocking")
  @DefaultValue("true")
  boolean isDisablePopupBlocking();

  // Timeouts
  @Key("wait.timeout")
  @DefaultValue("10")
  int waitTimeout();

  // Reporting
  @Key("screenshot.on.failure")
  @DefaultValue("true")
  boolean isScreenshotOnFailureEnabled();

  // Retry
  @Key("retry.enabled")
  @DefaultValue("false")
  boolean isRetryEnabled();

  @Key("retry.count")
  @DefaultValue("1")
  int retryCount();

  @Key("retry.delay.ms")
  @DefaultValue("0")
  int retryDelayMs();
}
