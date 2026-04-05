package io.github.jeihunn.data;

import io.github.jeihunn.data.generator.FakerDataGenerator;
import io.github.jeihunn.data.model.User;
import io.github.jeihunn.data.source.DataSource;
import io.github.jeihunn.data.source.JsonDataSource;
import java.util.Objects;
import java.util.Optional;

/** Central facade for static and dynamic test data access. */
public final class TestDataRegistry {

  private final DataSource dataSource;

  private FakerDataGenerator fakerDataGenerator;

  /** Creates a registry with the default JSON data source. */
  public TestDataRegistry() {
    this(new JsonDataSource());
  }

  /** Creates a registry with a custom data source implementation. */
  public TestDataRegistry(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource, "DataSource must not be null");
  }

  /** Reads typed data from a logical file and returns it as Optional. */
  private <T> Optional<T> getOptional(String fileName, String key, Class<T> clazz) {
    Objects.requireNonNull(fileName, "fileName must not be null");
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(clazz, "clazz must not be null");

    try {
      return Optional.ofNullable(dataSource.get(fileName, key, clazz));
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }

  /** Returns a test user entry from `users.json` by key. */
  public Optional<User> getUser(String key) {
    return getOptional("users.json", key, User.class);
  }

  /** Returns lazy-initialized faker-based data generator. */
  public FakerDataGenerator generate() {
    if (fakerDataGenerator == null) {
      fakerDataGenerator = new FakerDataGenerator();
    }
    return fakerDataGenerator;
  }
}
