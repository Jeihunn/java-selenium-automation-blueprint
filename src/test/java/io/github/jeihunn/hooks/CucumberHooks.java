package io.github.jeihunn.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.github.jeihunn.assertions.ReportingSoftAssertions;
import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.DriverManager;
import io.github.jeihunn.reporting.AllureEnvironmentWriter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Cucumber lifecycle hooks for driver setup, teardown, and failure reporting. */
public class CucumberHooks {

  private static final Logger LOG = LoggerFactory.getLogger(CucumberHooks.class);

  private final ReportingSoftAssertions softAssertions;

  /** Creates hook handlers with shared soft-assertion collector. */
  public CucumberHooks(ReportingSoftAssertions softAssertions) {
    this.softAssertions = softAssertions;
  }

  /** Writes Allure environment metadata once before all scenarios. */
  @BeforeAll
  public static void beforeAll() {
    AllureEnvironmentWriter.write();
  }

  /** Initializes WebDriver before each scenario. */
  @Before
  public void setUp(Scenario scenario) {
    DriverManager.getDriver();
    LOG.info("[START] Scenario: {}", scenario.getName());
  }

  /** Finalizes soft assertions, attaches failure screenshot, and quits driver. */
  @After
  public void tearDown(Scenario scenario) {

    WebDriver driver = DriverManager.getActiveDriver();
    boolean failed = scenario.isFailed();
    AssertionError softFailure = null;

    try {
      try {
        softAssertions.assertAll();
      } catch (AssertionError ae) {
        softFailure = ae;
        failed = true;
      }

      if (failed && ConfigurationManager.config().isScreenshotOnFailureEnabled()) {
        attachScreenshot(scenario, driver);
      }

    } finally {
      try {
        DriverManager.quitDriver();
      } catch (Exception e) {
        LOG.warn("Error while quitting driver: {}", e.toString(), e);
      }
    }

    LOG.info("[END] Scenario: {} | Status: {}", scenario.getName(), failed ? "FAILED" : "PASSED");

    if (softFailure != null) {
      throw softFailure;
    }
  }

  /** Attaches a screenshot to the scenario when supported by the active driver. */
  private void attachScreenshot(Scenario scenario, WebDriver driver) {
    if (!(driver instanceof TakesScreenshot screenshotDriver)) {
      return;
    }

    try {
      byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
      scenario.attach(screenshot, "image/png", "Scenario End Screenshot");
    } catch (Exception e) {
      LOG.warn("Failed to attach screenshot: {}", e.toString(), e);
    }
  }
}
