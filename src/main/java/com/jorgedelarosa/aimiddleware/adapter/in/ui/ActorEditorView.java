package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorEditorActorLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.GetActorDetailsUseCase;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import java.util.UUID;

/**
 * @author jorge
 */
@Route(value = "actor", layout = MainView.class)
public class ActorEditorView extends VerticalLayout
    implements HasDynamicTitle, HasUrlParameter<String> {

  private final GetActorDetailsUseCase getActorDetailsUseCase;

  private GetActorDetailsUseCase.ActorDto actorDto;
  private String pageTitle;

  public ActorEditorView(GetActorDetailsUseCase getActorDetailsUseCase) {
    this.getActorDetailsUseCase = getActorDetailsUseCase;
  }

  private void fillFormData() {
    Tab actorTab = new Tab(VaadinIcon.USER.create(), new Span("Actor"));
    actorTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    Tab mindTab = new Tab(VaadinIcon.ABACUS.create(), new Span("Mind"));
    mindTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    Tab outfitsTab = new Tab(VaadinIcon.GLASSES.create(), new Span("Outfits"));
    outfitsTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);

    TabSheet tabSheet = new TabSheet();
    tabSheet.add(actorTab, new ActorEditorActorLayout(actorDto));
    tabSheet.add(mindTab, new Div(new Text("This is the Mind tab content")));
    tabSheet.add(outfitsTab, new Div(new Text("This is the Outfits tab content")));

    Button primaryButton = new Button("Save");
    primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    add(tabSheet);
    add(primaryButton);
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    if (parameter != null) {
      actorDto =
          getActorDetailsUseCase.execute(
              new GetActorDetailsUseCase.Command(UUID.fromString(parameter)));
      pageTitle = "Actor Editor - " + actorDto.name();
    } else {
      actorDto = new GetActorDetailsUseCase.ActorDto(null, "", "");
      pageTitle = "Actor Editor - new";
    }
    fillFormData();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
