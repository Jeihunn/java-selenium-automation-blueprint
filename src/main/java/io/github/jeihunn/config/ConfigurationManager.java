package io.github.jeihunn.config;

import org.aeonbits.owner.ConfigCache;

/** Provides access to the cached {@link Configuration} instance. */
public final class ConfigurationManager {

  private ConfigurationManager() {}

  public static Configuration config() {
    return ConfigCache.getOrCreate(Configuration.class);
  }
}
