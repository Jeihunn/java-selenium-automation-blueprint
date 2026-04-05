package io.github.jeihunn.utils;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeihunn.config.SecretResolver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Strict JSON mapper with secret placeholder injection support. */
public final class JsonMapper {

  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
          .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(\\w+)}");

  private JsonMapper() {}

  /** Reads a classpath JSON file and maps it to {@code Map<String, T>}. */
  public static <T> Map<String, T> readAsMap(String filePath, Class<T> clazz) {
    try (var inputStream = getResourceStream(filePath)) {
      var jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      var processedJson = injectSecrets(jsonContent);

      return MAPPER.readValue(
          processedJson, MAPPER.getTypeFactory().constructMapType(Map.class, String.class, clazz));
    } catch (IOException e) {
      throw new IllegalStateException("Failed to parse JSON file: " + filePath, e);
    }
  }

  /** Returns a classpath resource stream or throws if the file is missing. */
  private static InputStream getResourceStream(String filePath) {
    var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    if (inputStream == null) {
      throw new IllegalStateException("File not found on classpath: " + filePath);
    }
    return inputStream;
  }

  /** Resolves and injects ${KEY} placeholders into JSON content. */
  private static String injectSecrets(String content) {
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(content);
    StringBuilder sb = new StringBuilder(content.length());

    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      boolean leftQuote = start > 0 && content.charAt(start - 1) == '"';
      boolean rightQuote = end < content.length() && content.charAt(end) == '"';

      if (!(leftQuote && rightQuote)) {
        throw new IllegalStateException(
            "Invalid placeholder usage near: "
                + matcher.group(0)
                + ". "
                + "Placeholders must be used as quoted string values, e.g. \"${KEY}\".");
      }

      String key = matcher.group(1);
      String rawSecret = SecretResolver.resolve(key);

      String escapedSecret = new String(JsonStringEncoder.getInstance().quoteAsString(rawSecret));

      matcher.appendReplacement(sb, Matcher.quoteReplacement(escapedSecret));
    }

    matcher.appendTail(sb);
    return sb.toString();
  }
}
