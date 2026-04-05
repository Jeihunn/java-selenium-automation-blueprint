package io.github.jeihunn.retry.testng;

import io.github.jeihunn.config.Configuration;
import io.github.jeihunn.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** TestNG retry analyzer that re-runs failed tests based on configuration. */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);
  private static final Configuration CONFIG = ConfigurationManager.config();
  private static final boolean RETRY_ENABLED = CONFIG.isRetryEnabled();
  private static final int MAX_RETRY_COUNT = CONFIG.retryCount();
  private static final int RETRY_DELAY_MS = CONFIG.retryDelayMs();

  static {
    validateRetryConfiguration();
  }

  private int retryCount = 0;

  /** Returns true when a failed test should be retried. */
  @Override
  public boolean retry(ITestResult result) {

    if (!RETRY_ENABLED) {
      return false;
    }

    if (!result.isSuccess() && retryCount < MAX_RETRY_COUNT) {

      retryCount++;

      LOG.warn(
          "[RETRY] Attempt {}/{} | Delay={}ms | Thread={}",
          retryCount,
          MAX_RETRY_COUNT,
          RETRY_DELAY_MS,
          Thread.currentThread().threadId());

      return sleepBeforeRetry();
    }

    return false;
  }

  private static void validateRetryConfiguration() {
    if (MAX_RETRY_COUNT < 0) {
      throw new IllegalStateException("retry.count must be greater than or equal to 0");
    }

    if (RETRY_DELAY_MS < 0) {
      throw new IllegalStateException("retry.delay.ms must be greater than or equal to 0");
    }
  }

  private boolean sleepBeforeRetry() {
    if (RETRY_DELAY_MS <= 0) {
      return true;
    }

    try {
      Thread.sleep(RETRY_DELAY_MS);
      return true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOG.warn("[RETRY] Retry delay interrupted after {} ms", RETRY_DELAY_MS, e);
      return false;
    }
  }
}
