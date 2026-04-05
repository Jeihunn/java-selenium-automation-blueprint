package io.github.jeihunn.ui.pages;

import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import java.util.List;
import org.openqa.selenium.By;

/** Checkout page actions and summary data helpers. */
public class CheckoutPage extends BasePage {

  // Information Page
  private static final By FIRST_NAME_INPUT = By.id("first-name");
  private static final By LAST_NAME_INPUT = By.id("last-name");
  private static final By ZIP_CODE_INPUT = By.id("postal-code");
  private static final By CONTINUE_BUTTON = By.id("continue");
  private static final By CANCEL_BUTTON = By.id("cancel");
  private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

  // Overview Page
  private static final By OVERVIEW_ITEMS = By.className("inventory_item_name");
  private static final By ITEM_TOTAL_LABEL = By.className("summary_subtotal_label");
  private static final By TAX_LABEL = By.className("summary_tax_label");
  private static final By TOTAL_LABEL = By.className("summary_total_label");
  private static final By FINISH_BUTTON = By.id("finish");

  // Complete Page
  private static final By COMPLETE_HEADER = By.className("complete-header");
  private static final By BACK_HOME_BUTTON = By.id("back-to-products");

  /** Creates the checkout page with the shared driver instance. */
  public CheckoutPage(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Enters the first name on the checkout information step. */
  public void enterFirstName(String firstName) {
    type(FIRST_NAME_INPUT, firstName);
  }

  /** Enters the last name on the checkout information step. */
  public void enterLastName(String lastName) {
    type(LAST_NAME_INPUT, lastName);
  }

  /** Enters the ZIP or postal code on the checkout information step. */
  public void enterZipCode(String zipCode) {
    type(ZIP_CODE_INPUT, zipCode);
  }

  /** Proceeds from checkout information to overview. */
  public void clickContinue() {
    click(CONTINUE_BUTTON);
  }

  /** Cancels checkout and returns to the previous page. */
  public void clickCancel() {
    click(CANCEL_BUTTON);
  }

  /** Completes the order on the overview step. */
  public void clickFinish() {
    click(FINISH_BUTTON);
  }

  /** Returns to inventory from the checkout complete page. */
  public void clickBackHome() {
    click(BACK_HOME_BUTTON);
  }

  /** Returns the checkout validation error message. */
  public String getErrorMessage() {
    return getText(ERROR_MESSAGE);
  }

  /** Returns product names listed on the checkout overview page. */
  public List<String> getOverviewProductNames() {
    return getTexts(OVERVIEW_ITEMS);
  }

  /** Returns the item total label text from checkout overview. */
  public String getItemTotal() {
    return getText(ITEM_TOTAL_LABEL);
  }

  /** Returns the tax label text from checkout overview. */
  public String getTax() {
    return getText(TAX_LABEL);
  }

  /** Returns the final total label text from checkout overview. */
  public String getTotal() {
    return getText(TOTAL_LABEL);
  }

  /** Returns the completion header message after finishing checkout. */
  public String getCompleteMessage() {
    return getText(COMPLETE_HEADER);
  }
}
