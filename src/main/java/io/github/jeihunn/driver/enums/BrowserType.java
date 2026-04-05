package io.github.jeihunn.driver.enums;

import java.util.Locale;

/** Supported browser types. */
public enum BrowserType {
  CHROME,
  FIREFOX,
  EDGE;

  public static BrowserType from(String value) {
    if (value == null || value.isBlank()) {
      return CHROME;
    }

    return switch (value.trim().toLowerCase(Locale.ROOT)) {
      case "chrome" -> CHROME;
      case "firefox", "ff" -> FIREFOX;
      case "edge", "msedge" -> EDGE;
      default -> throw new IllegalArgumentException("Unsupported browser: " + value);
    };
  }
}
