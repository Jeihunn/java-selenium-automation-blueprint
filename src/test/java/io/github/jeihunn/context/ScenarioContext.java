package io.github.jeihunn.context;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Scenario-scoped key-value storage for sharing data across step definitions. */
public final class ScenarioContext {

  private final Map<ScenarioContextKey, Object> storage = new EnumMap<>(ScenarioContextKey.class);

  /** Stores a value in the scenario context. */
  public <T> void set(ScenarioContextKey key, T value) {
    storage.put(Objects.requireNonNull(key, "Context key must not be null"), value);
  }

  /** Retrieves a value from the scenario context. */
  @SuppressWarnings("unchecked")
  public <T> T get(ScenarioContextKey key) {
    return (T) storage.get(Objects.requireNonNull(key, "Context key must not be null"));
  }

  /** Checks if a key exists in the context. */
  public boolean contains(ScenarioContextKey key) {
    return storage.containsKey(Objects.requireNonNull(key, "Context key must not be null"));
  }

  /** Clears all scenario-scoped data. */
  public void clear() {
    storage.clear();
  }
}
