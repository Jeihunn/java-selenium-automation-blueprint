package io.github.jeihunn.assertions;

import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Soft assertions wrapper that reports failures to Allure with optional screenshots. */
public class ReportingSoftAssertions extends SoftAssertions {

  private static final Logger LOG = LoggerFactory.getLogger(ReportingSoftAssertions.class);
  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");

  /** Records each collected soft assertion failure as an Allure step. */
  @Override
  public void onAssertionErrorCollected(AssertionError error) {
    LOG.warn("[SOFT-ASSERT] {}", error.getMessage());
    recordFailure(error.getMessage());
  }

  /** Creates a failed Allure step and attaches failure details. */
  private void recordFailure(String message) {
    String stepUuid = UUID.randomUUID().toString();
    StepResult stepResult =
        new StepResult().setName("Soft assertion failed").setStatus(Status.FAILED);

    Allure.getLifecycle().startStep(stepUuid, stepResult);

    try {
      attachText("Assertion Message", message);

      if (ConfigurationManager.config().isScreenshotOnFailureEnabled()) {
        attachScreenshot();
      }
    } finally {
      Allure.getLifecycle().stopStep(stepUuid);
    }
  }

  /** Attaches plain-text details to the current Allure step. */
  private void attachText(String name, String content) {
    Allure.addAttachment(name, "text/plain", content);
  }

  /** Captures and attaches a screenshot to the current Allure step. */
  private void attachScreenshot() {
    WebDriver driver = DriverManager.getActiveDriver();
    if (!(driver instanceof TakesScreenshot screenshotDriver)) {
      LOG.debug("Driver doesn't support screenshots, skipping.");
      return;
    }

    try {
      byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
      String name = "Screenshot - " + LocalDateTime.now().format(TIMESTAMP_FORMAT);

      Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
    } catch (Exception e) {
      LOG.warn("Failed to capture screenshot on soft assertion failure.", e);
    }
  }
}
