package io.github.jeihunn.ui;

import io.github.jeihunn.config.ConfigurationManager;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared UI actions and wait utilities for page objects. */
public abstract class BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

  private static final int RETRY_COUNT = 3;
  private static final Duration POLLING_INTERVAL = Duration.ofMillis(400);
  private static final Duration RETRY_BACKOFF = Duration.ofMillis(200);

  protected final WebDriver driver;
  protected final Wait<WebDriver> wait;
  protected final Actions actions;
  protected final JavascriptExecutor js;
  protected final int defaultTimeout;

  /** Creates a base page with shared wait, actions, and JavaScript helpers. */
  protected BasePage(WebDriver driver) {
    this.driver = Objects.requireNonNull(driver, "WebDriver must not be null");
    this.defaultTimeout = ConfigurationManager.config().waitTimeout();

    this.wait =
        new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout))
            .pollingEvery(POLLING_INTERVAL)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class);

    this.actions = new Actions(driver);
    this.js = (JavascriptExecutor) driver;
  }

  /** Waits until the element is visible and returns it. */
  protected WebElement waitForVisible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  /** Waits until the element is clickable and returns it. */
  protected WebElement waitForClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  /** Waits until the element is not visible or removed from the DOM. */
  protected void waitForInvisible(By locator) {
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
  }

  /** Waits until the current URL contains the expected value. */
  protected void waitForUrlContains(String value) {
    wait.until(ExpectedConditions.urlContains(value));
  }

  /** Waits until `document.readyState` becomes `complete`. */
  protected void waitForPageLoad() {
    wait.until(
        d -> "complete".equals(String.valueOf(js.executeScript("return document.readyState"))));
  }

  /** Finds and returns a single element (waits for it to be visible first). */
  protected WebElement find(By locator) {
    return waitForVisible(locator);
  }

  /** Waits for presence and returns all elements matching the locator. */
  protected List<WebElement> findAll(By locator) {
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
  }

  /** Clicks an element with retry support and JavaScript fallback. */
  protected void click(By locator) {
    retryOperation(
        d -> {
          WebElement element = waitForClickable(locator);
          ensureInView(element);

          try {
            element.click();
          } catch (ElementClickInterceptedException e) {
            LOG.debug("Normal click intercepted. Trying Actions click...");
            actions.moveToElement(element).click().perform();
          }
          return null;
        },
        locator,
        "click",
        () -> {
          // Final fallback for intercepted clicks.
          WebElement element = driver.findElement(locator);
          ensureInView(element);
          js.executeScript("arguments[0].click();", element);
          return null;
        });
  }

  /** Clicks an element directly via JavaScript with retry support. */
  protected void jsClick(By locator) {
    retryOperation(
        d -> {
          WebElement element = waitForVisible(locator);
          ensureInView(element);
          js.executeScript("arguments[0].click();", element);
          return null;
        },
        locator,
        "jsClick",
        null);
  }

  /** Clears an input and types the provided text. */
  protected void type(By locator, String text) {
    retryOperation(
        d -> {
          WebElement element = waitForVisible(locator);
          ensureInView(element);
          clearUsingKeyboard(element);
          element.sendKeys(text);
          return null;
        },
        locator,
        "type",
        null);
  }

  /** Returns trimmed visible text from the target element. */
  protected String getText(By locator) {
    return retryOperation(d -> waitForVisible(locator).getText().trim(), locator, "getText", null);
  }

  /** Returns trimmed texts from all matched elements. */
  protected List<String> getTexts(By locator) {
    return retryOperation(
        d -> findAll(locator).stream().map(WebElement::getText).map(String::trim).toList(),
        locator,
        "getTexts",
        null);
  }

  /** Returns the requested attribute value from a visible element. */
  protected String getAttribute(By locator, String attribute) {
    return retryOperation(
        d -> waitForVisible(locator).getAttribute(attribute), locator, "getAttribute", null);
  }

  /** Selects an option by `value` in a standard `<select>` element. */
  protected void selectByValue(By locator, String value) {
    retryOperation(
        d -> {
          new Select(waitForVisible(locator)).selectByValue(value);
          return null;
        },
        locator,
        "selectByValue",
        null);
  }

  /** Selects an option by visible text in a standard `<select>` element. */
  protected void selectByVisibleText(By locator, String text) {
    retryOperation(
        d -> {
          new Select(waitForVisible(locator)).selectByVisibleText(text);
          return null;
        },
        locator,
        "selectByVisibleText",
        null);
  }

  /** Moves the mouse over the target element. */
  protected void hover(By locator) {
    retryOperation(
        d -> {
          actions.moveToElement(waitForVisible(locator)).perform();
          return null;
        },
        locator,
        "hover",
        null);
  }

  /** Returns true if at least one matching element exists in the DOM. */
  protected boolean isPresent(By locator) {
    return !driver.findElements(locator).isEmpty();
  }

  /** Returns true if any matching element is currently displayed. */
  protected boolean isDisplayed(By locator) {
    List<WebElement> elements = driver.findElements(locator);
    return elements.stream().anyMatch(WebElement::isDisplayed);
  }

  /** Returns true if the visible element is enabled for interaction. */
  protected boolean isEnabled(By locator) {
    return Boolean.TRUE.equals(
        retryOperation(d -> waitForVisible(locator).isEnabled(), locator, "isEnabled", null));
  }

  /** Scrolls the element into view if it is outside the viewport. */
  private void ensureInView(WebElement element) {
    Boolean inView =
        (Boolean)
            js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();"
                    + "return (rect.top >= 0 && rect.bottom <= window.innerHeight);",
                element);

    if (Boolean.FALSE.equals(inView)) {
      js.executeScript(
          "arguments[0].scrollIntoView({block:'center',behavior:'instant'});", element);
    }
  }

  /** Clears an input using the platform-specific select-all shortcut. */
  private void clearUsingKeyboard(WebElement element) {
    Keys control =
        System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")
            ? Keys.COMMAND
            : Keys.CONTROL;

    element.sendKeys(Keys.chord(control, "a"));
    element.sendKeys(Keys.DELETE);
  }

  /** Retries transient interaction failures and applies an optional fallback action. */
  private <T> T retryOperation(
      Function<WebDriver, T> operation, By locator, String actionName, Supplier<T> fallbackAction) {

    RuntimeException lastException =
        new IllegalStateException("Unexpected state in retry mechanism");

    for (int attempt = 1; attempt <= RETRY_COUNT; attempt++) {
      try {
        return operation.apply(driver);
      } catch (StaleElementReferenceException | ElementNotInteractableException e) {

        lastException =
            new RuntimeException(
                String.format(
                    "Action '%s' failed on %s | Attempt %d/%d",
                    actionName, locator, attempt, RETRY_COUNT),
                e);

        LOG.debug(
            "Retry {}/{} for {} due to {}",
            attempt,
            RETRY_COUNT,
            locator,
            e.getClass().getSimpleName());

        sleep(RETRY_BACKOFF);
      }
    }

    if (fallbackAction != null) {
      LOG.debug("Applying fallback action for {}", locator);
      try {
        return fallbackAction.get();
      } catch (WebDriverException e) {
        throw new IllegalStateException("Fallback action failed for locator: " + locator, e);
      }
    }

    LOG.error("All retry attempts exhausted for {}", locator);
    throw lastException;
  }

  /** Sleeps for a short backoff interval between retries. */
  private void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
