package io.github.jeihunn.reporting;

import io.github.jeihunn.config.Configuration;
import io.github.jeihunn.config.ConfigurationManager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Writes environment metadata to `allure-results/environment.properties`. */
public final class AllureEnvironmentWriter {

  private static final Logger LOG = LoggerFactory.getLogger(AllureEnvironmentWriter.class);

  private static final String DEFAULT_RESULTS_DIR = "target/allure-results";

  private static volatile boolean written = false;

  private AllureEnvironmentWriter() {}

  /** Writes Allure environment properties once per test run. */
  public static synchronized void write() {
    if (written) {
      return;
    }

    try {
      Path resultsDir = resolveResultsDirectory();
      Map<String, String> envData = buildEnvironment();
      writeFile(resultsDir, envData);

      written = true;
      LOG.info("Allure environment.properties written");

    } catch (Exception e) {
      LOG.warn("Unable to write Allure environment.properties", e);
    }
  }

  /** Writes `environment.properties` into the resolved results directory. */
  private static void writeFile(Path resultsDir, Map<String, String> environment)
      throws IOException {

    Path file = resultsDir.resolve("environment.properties");

    try (BufferedWriter writer =
        Files.newBufferedWriter(
            file,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {

      writer.write("# Allure Environment Configuration");
      writer.newLine();

      for (Map.Entry<String, String> entry : environment.entrySet()) {
        if (entry.getKey() != null && entry.getValue() != null) {
          writer.write(escapeKey(entry.getKey()));
          writer.write('=');
          writer.write(entry.getValue());
          writer.newLine();
        }
      }
    }
  }

  /** Builds environment key-value pairs from runtime configuration. */
  private static Map<String, String> buildEnvironment() {
    Map<String, String> env = new LinkedHashMap<>();

    Configuration config = ConfigurationManager.config();

    env.put("Execution Mode", config.executionMode());
    env.put("Base URL", config.baseUrl());
    env.put("Browser", config.browser());
    env.put("Browser Headless", String.valueOf(config.isHeadless()));
    env.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));

    return env;
  }

  /** Escapes property keys to match Java `.properties` format rules. */
  private static String escapeKey(String key) {
    return key.replace("\\", "\\\\").replace(" ", "\\ ");
  }

  /** Resolves and creates the configured Allure results directory. */
  private static Path resolveResultsDirectory() throws IOException {
    String dir = System.getProperty("allure.results.directory", DEFAULT_RESULTS_DIR);

    Path path = Paths.get(dir);
    Files.createDirectories(path);
    return path;
  }
}
