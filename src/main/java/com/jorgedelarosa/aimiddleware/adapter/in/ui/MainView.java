package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.SideNavigation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.List;

// From https://vaadin.com/docs/latest/flow/application/main-view

// TODO i18n

/**
 * @author jorge
 */
@RouteAlias(value = "main", layout = MainView.class)
@Route("")
@PageTitle("Main")
public class MainView extends AppLayout implements AfterNavigationObserver {

  private H1 viewTitle;

  public MainView() {
    // Use the drawer for the menu
    setPrimarySection(Section.DRAWER);

    addToNavbar(true, createHeaderContent());

    addToDrawer(createDrawerContent());
  }

  private Component createHeaderContent() {
    HorizontalLayout layout = new HorizontalLayout();

    // Configure styling for the header
    layout.setId("header");
    layout.getThemeList().set("dark", true);
    layout.setWidthFull();
    layout.setSpacing(false);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);

    // Have the drawer toggle button on the left
    layout.add(new DrawerToggle());

    // Placeholder for the title of the current view.
    // The title will be set after navigation.
    viewTitle = new H1();
    layout.add(viewTitle);

    // TODO A user icon
    // layout.add(new Image("images/user.svg", "Avatar"));

    return layout;
  }

  private Component createDrawerContent() {
    VerticalLayout layout = new VerticalLayout();

    // Configure styling for the drawer
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.getThemeList().set("spacing-s", true);
    layout.setAlignItems(FlexComponent.Alignment.STRETCH);

    // Have a drawer header with an application logo
    HorizontalLayout logoLayout = new HorizontalLayout();
    logoLayout.setId("logo");
    logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    // logoLayout.add(new Image("images/logo.png", "My Project logo")); // TODO
    logoLayout.add(new H1("Artifantasy"));

    // Display the logo and the menu in the drawer
    layout.add(logoLayout, new SideNavigation());
    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    String pageTitle = "error";

    // Get list of current views, the first view is the top view.
    List<HasElement> views = UI.getCurrent().getInternals().getActiveRouterTargetsChain();
    if (!views.isEmpty()) {
      HasElement view = views.get(0);

      // If the view has a dynamic title we'll use that
      if (view instanceof HasDynamicTitle hasDynamicTitle) {
        pageTitle = hasDynamicTitle.getPageTitle();
      } else if (getContent() != null) {
        // It does not have a dynamic title. Try to read title from
        // annotations
        pageTitle = getContent().getClass().getAnnotation(PageTitle.class).value();
      } else {
        pageTitle = "Main";
      }
    }

    // Set the view title in the header
    viewTitle.setText(pageTitle);
  }
}
