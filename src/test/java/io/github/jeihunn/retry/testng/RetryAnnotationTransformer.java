package io.github.jeihunn.retry.testng;

import io.github.jeihunn.config.ConfigurationManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/** Applies the retry analyzer to TestNG tests when retry is enabled. */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

  /** Sets retry analyzer on test annotations based on runtime configuration. */
  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

    if (ConfigurationManager.config().isRetryEnabled()) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
