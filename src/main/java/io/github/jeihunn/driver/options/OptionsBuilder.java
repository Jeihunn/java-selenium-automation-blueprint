package io.github.jeihunn.driver.options;

import io.github.jeihunn.driver.config.DriverConfig;

/** Builds browser-specific options from the shared driver configuration. */
public interface OptionsBuilder<T> {
  T build(DriverConfig config);
}
