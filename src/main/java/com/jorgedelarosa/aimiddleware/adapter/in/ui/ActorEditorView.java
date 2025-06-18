package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
@Route(value = "actor", layout = MainView.class)
public class ActorEditorView extends VerticalLayout
    implements HasDynamicTitle, HasUrlParameter<String> {

  private Optional<UUID> actorId;
  private String pageTitle;

  public ActorEditorView() {
    Tab actorTab = new Tab(VaadinIcon.USER.create(), new Span("Actor"));
    Tab mindTab = new Tab(VaadinIcon.ABACUS.create(), new Span("Mind"));
    Tab outfitsTab = new Tab(VaadinIcon.GLASSES.create(), new Span("Outfits"));

    // Set the icon on top
    for (Tab tab : new Tab[] {actorTab, mindTab, outfitsTab}) {
      tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    }

    Tabs tabs = new Tabs(actorTab, mindTab, outfitsTab);
    add(tabs);
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    if (parameter != null) {
      actorId = Optional.of(UUID.fromString(parameter));
      pageTitle = "Actor Editor - " + parameter;
    } else {
      actorId = Optional.empty();
      pageTitle = "Actor Editor - new";
    }
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
