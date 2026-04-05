package io.github.jeihunn.driver.factory;

import io.github.jeihunn.driver.config.DriverConfig;
import io.github.jeihunn.driver.enums.ExecutionMode;

/** Resolves the driver factory based on the configured execution mode. */
public final class DriverFactoryResolver {

  private DriverFactoryResolver() {}

  public static DriverFactory getFactory(DriverConfig config) {

    ExecutionMode mode = config.mode();

    return switch (mode) {
      case LOCAL -> new LocalDriverFactory(config);
      case GRID -> new GridDriverFactory(config);
      case CLOUD ->
          throw new UnsupportedOperationException(
              "CLOUD mode is not implemented yet. Add CloudDriverFactory when ready.");
    };
  }
}
