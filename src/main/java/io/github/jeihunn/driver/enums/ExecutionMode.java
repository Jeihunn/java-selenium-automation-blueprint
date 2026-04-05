package io.github.jeihunn.driver.enums;

import java.util.Locale;

/** Supported execution modes. */
public enum ExecutionMode {
  LOCAL,
  GRID,
  CLOUD;

  public static ExecutionMode from(String value) {
    if (value == null || value.isBlank()) {
      return LOCAL;
    }

    return switch (value.trim().toLowerCase(Locale.ROOT)) {
      case "local" -> LOCAL;
      case "grid" -> GRID;
      case "cloud" -> CLOUD;
      default -> throw new IllegalArgumentException("Unsupported execution.mode: " + value);
    };
  }
}
