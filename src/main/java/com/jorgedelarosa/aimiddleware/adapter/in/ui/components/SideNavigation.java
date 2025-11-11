package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.ActorEditorView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.ActorImportView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.ActorsListView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.OutfitEditorView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.OutfitListView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.ScenarioEditorView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.ScenariosListView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.SessionEditorView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.SessionsListView;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.UserSettingsView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/**
 * @author jorge
 */
public class SideNavigation extends Div {

  public SideNavigation() {
    SideNav mainSection = new SideNav("Main");

    SideNavItem actors =
        new SideNavItem("Actors", ActorsListView.class, VaadinIcon.FAMILY.create());
    actors.addItem(new SideNavItem("New actor", ActorEditorView.class, VaadinIcon.PLUS.create()));
    actors.addItem(
        new SideNavItem("Import actor", ActorImportView.class, VaadinIcon.ARROW_DOWN.create()));
    SideNavItem outfits =
        new SideNavItem("Outfits", OutfitListView.class, VaadinIcon.GLASSES.create());
    outfits.addItem(
        new SideNavItem("New outfit", OutfitEditorView.class, VaadinIcon.PLUS.create()));
    actors.addItem(outfits);
    mainSection.addItem(actors);

    SideNavItem scenarios =
        new SideNavItem("Scenarios", ScenariosListView.class, VaadinIcon.PICTURE.create());
    scenarios.addItem(
        new SideNavItem("New scenario", ScenarioEditorView.class, VaadinIcon.PLUS.create()));
    mainSection.addItem(scenarios);

    SideNavItem sessions =
        new SideNavItem("Sessions", SessionsListView.class, VaadinIcon.CHAT.create());
    sessions.addItem(
        new SideNavItem("New session", SessionEditorView.class, VaadinIcon.PLUS.create()));
    mainSection.addItem(sessions);
    add(mainSection);

    SideNav userSection = new SideNav("User");

    SideNavItem settings =
        new SideNavItem("Settings", UserSettingsView.class, VaadinIcon.TOOLS.create());
    userSection.addItem(settings);

    Div navWrapper = new Div(mainSection, userSection);
    mainSection.setWidthFull();
    userSection.setWidthFull();
    add(navWrapper);
  }
}
