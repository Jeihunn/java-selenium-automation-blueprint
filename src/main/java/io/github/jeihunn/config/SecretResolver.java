package io.github.jeihunn.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

/** Resolves secrets from JVM properties, OS environment variables, and .env. */
public final class SecretResolver {

  private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

  private SecretResolver() {}

  /** Resolves a non-empty secret value for the given key. */
  public static String resolve(String key) {

    Objects.requireNonNull(key, "Secret key must not be null");

    String normalizedKey = key.trim();
    if (normalizedKey.isEmpty()) {
      throw new IllegalArgumentException("Secret key must not be blank");
    }

    String value = System.getProperty(normalizedKey);

    if (value == null) {
      value = System.getenv(normalizedKey);
    }

    if (value == null) {
      value = DOTENV.get(normalizedKey);
    }

    if (value == null || value.isBlank()) {
      throw new IllegalStateException(
          "Secret not found: "
              + normalizedKey
              + ". "
              + "Provide it via JVM (-D"
              + normalizedKey
              + "=...), OS environment variable, or local .env file.");
    }

    return value;
  }
}
