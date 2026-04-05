package io.github.jeihunn.steps;

import io.cucumber.java.en.When;
import io.github.jeihunn.ui.components.SidebarComponent;

/** Step definitions for sidebar menu actions. */
public class SidebarSteps {

  private final SidebarComponent sidebar;

  /** Creates sidebar steps with the shared sidebar component. */
  public SidebarSteps(SidebarComponent sidebar) {
    this.sidebar = sidebar;
  }

  @When("the user selects {string} from the sidebar menu")
  public void selectSidebarMenuItem(String menuItem) {
    sidebar.clickMenuItem(menuItem);
  }
}
