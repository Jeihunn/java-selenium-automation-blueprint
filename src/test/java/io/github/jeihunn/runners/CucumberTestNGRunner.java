package io.github.jeihunn.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/** TestNG runner for Cucumber scenarios. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"io.github.jeihunn.steps", "io.github.jeihunn.hooks"},
    plugin = {
      "pretty",
      "summary",
      "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
      "json:target/cucumber-reports/cucumber.json",
      "html:target/cucumber-reports/cucumber.html",
      "rerun:target/cucumber-reports/rerun.txt"
    },
    monochrome = true)
public class CucumberTestNGRunner extends AbstractTestNGCucumberTests {

  /** Enables parallel execution of Cucumber scenarios. */
  @Override
  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    return super.scenarios();
  }
}
