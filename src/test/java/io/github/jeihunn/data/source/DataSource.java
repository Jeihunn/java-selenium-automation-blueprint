package io.github.jeihunn.data.source;

/** Contract for retrieving typed test data values from a backing source. */
public interface DataSource {
  /** Returns a mapped value by logical file name and key. */
  <T> T get(String logicalFileName, String key, Class<T> clazz);
}
