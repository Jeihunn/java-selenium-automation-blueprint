package io.github.jeihunn.data.loader;

import io.github.jeihunn.utils.JsonMapper;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Loads and caches JSON test data by file path and target model class. */
public final class JsonDataLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JsonDataLoader.class);

  private record CacheKey(String filePath, Class<?> clazz) {}

  private static final Map<CacheKey, Map<String, ?>> CACHE = new ConcurrentHashMap<>();

  private JsonDataLoader() {
    throw new UnsupportedOperationException("Utility class");
  }

  /** Returns mapped test data by key from the specified JSON file. */
  @SuppressWarnings("unchecked")
  public static <T> T get(String filePath, String key, Class<T> clazz) {

    Objects.requireNonNull(filePath, "filePath must not be null");
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(clazz, "clazz must not be null");

    CacheKey cacheKey = new CacheKey(filePath, clazz);

    Map<String, T> data =
        (Map<String, T>)
            CACHE.computeIfAbsent(
                cacheKey,
                k -> {
                  LOG.info(
                      "[DATA-LOADER] Caching JSON | File: {} | Class: {}",
                      k.filePath(),
                      k.clazz().getSimpleName());
                  return JsonMapper.readAsMap(k.filePath(), (Class<T>) k.clazz());
                });

    T value = data.get(key);

    if (value == null) {
      throw new IllegalArgumentException(
          String.format(
              "Test data not found! File: '%s', Key: '%s', Class: '%s'",
              filePath, key, clazz.getSimpleName()));
    }

    return value;
  }

  /** Clears all cached JSON test data. */
  public static void clearCache() {
    CACHE.clear();
    LOG.info("[DATA-LOADER] Cache cleared");
  }
}
