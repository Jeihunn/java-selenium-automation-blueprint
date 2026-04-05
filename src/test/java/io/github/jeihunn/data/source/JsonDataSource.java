package io.github.jeihunn.data.source;

import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.data.loader.JsonDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** JSON-backed data source with environment-aware fallback to common data. */
public final class JsonDataSource implements DataSource {

  private static final Logger LOG = LoggerFactory.getLogger(JsonDataSource.class);

  private static final String BASE_DATA_PATH = "testdata/";
  private static final String COMMON_FOLDER = "common/";

  private final String envFolder;

  /** Creates a JSON data source using the active runtime environment. */
  public JsonDataSource() {
    String env = ConfigurationManager.config().env();
    this.envFolder = (env == null || env.isBlank()) ? COMMON_FOLDER : env.trim() + "/";
  }

  /** Resolves data from env-specific file first, then falls back to common. */
  @Override
  public <T> T get(String logicalFileName, String key, Class<T> clazz) {
    String envPath = BASE_DATA_PATH + envFolder + logicalFileName;

    if (COMMON_FOLDER.equals(envFolder)) {
      return JsonDataLoader.get(envPath, key, clazz);
    }

    if (resourceExists(envPath)) {
      return JsonDataLoader.get(envPath, key, clazz);
    }

    String fallbackPath = BASE_DATA_PATH + COMMON_FOLDER + logicalFileName;
    LOG.debug("File '{}' not found, falling back to '{}'", envPath, fallbackPath);

    return JsonDataLoader.get(fallbackPath, key, clazz);
  }

  private boolean resourceExists(String path) {
    return Thread.currentThread().getContextClassLoader().getResource(path) != null;
  }
}
