package io.github.jeihunn.data.generator;

import java.util.Locale;
import net.datafaker.Faker;

/** Generates dynamic and randomized test data. */
public final class FakerDataGenerator {

  private final Faker faker;

  /** Creates a US-locale faker instance for test data generation. */
  public FakerDataGenerator() {
    this.faker = new Faker(Locale.US);
  }

  /** Generates a strong random password for test scenarios. */
  public String getPassword() {
    return faker.credentials().password(10, 15, true, true, true);
  }

  /** Returns the underlying faker instance for custom data generation. */
  public Faker raw() {
    return faker;
  }
}
