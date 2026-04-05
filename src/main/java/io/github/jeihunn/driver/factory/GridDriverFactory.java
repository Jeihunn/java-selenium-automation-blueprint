package io.github.jeihunn.driver.factory;

import io.github.jeihunn.config.Configuration;
import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.config.DriverConfig;
import io.github.jeihunn.driver.enums.BrowserType;
import io.github.jeihunn.driver.options.ChromeOptionsBuilder;
import io.github.jeihunn.driver.options.EdgeOptionsBuilder;
import io.github.jeihunn.driver.options.FirefoxOptionsBuilder;
import java.net.URI;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

/** Creates remote drivers for Selenium Grid execution. */
public final class GridDriverFactory implements DriverFactory {

  private final DriverConfig config;

  public GridDriverFactory(DriverConfig config) {
    this.config = config;
  }

  @Override
  public WebDriver create() {
    try {
      URL gridUrl = buildGridUrl();
      BrowserType browser = config.browser();

      RemoteWebDriver driver =
          switch (browser) {
            case CHROME -> new RemoteWebDriver(gridUrl, new ChromeOptionsBuilder().build(config));
            case FIREFOX -> new RemoteWebDriver(gridUrl, new FirefoxOptionsBuilder().build(config));
            case EDGE -> new RemoteWebDriver(gridUrl, new EdgeOptionsBuilder().build(config));
          };

      driver.setFileDetector(new LocalFileDetector());

      return driver;

    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to initialize RemoteWebDriver using Selenium Grid", e);
    }
  }

  private URL buildGridUrl() throws Exception {
    Configuration runtimeConfig = ConfigurationManager.config();
    String gridHost = normalize(runtimeConfig.gridHost());
    String gridUsername = normalize(runtimeConfig.gridUsername());
    String gridPassword = normalize(runtimeConfig.gridPassword());

    if (gridHost == null) {
      throw new IllegalStateException("Grid execution requires GRID_HOST.");
    }

    URI hostUri = URI.create(gridHost);

    if (hostUri.getScheme() == null || hostUri.getHost() == null) {
      throw new IllegalStateException(
          "GRID_HOST must be an absolute URL, e.g. http://localhost:4444");
    }

    if (hostUri.getUserInfo() != null) {
      throw new IllegalStateException(
          "GRID_HOST must not contain credentials. Use GRID_USERNAME and GRID_PASSWORD.");
    }

    String path = hostUri.getPath();
    String gridPath = (path == null || path.isBlank() || "/".equals(path)) ? "/wd/hub" : path;

    if (gridUsername == null && gridPassword == null) {
      return new URI(
              hostUri.getScheme(), null, hostUri.getHost(), hostUri.getPort(), gridPath, null, null)
          .toURL();
    }

    if (gridUsername == null || gridPassword == null) {
      throw new IllegalStateException(
          "Provide GRID_USERNAME and GRID_PASSWORD together, or omit both.");
    }

    return new URI(
            hostUri.getScheme(),
            gridUsername + ":" + gridPassword,
            hostUri.getHost(),
            hostUri.getPort(),
            gridPath,
            null,
            null)
        .toURL();
  }

  private String normalize(String value) {
    if (value == null) {
      return null;
    }

    String normalized = value.trim();
    return normalized.isEmpty() ? null : normalized;
  }
}
