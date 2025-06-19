package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorEditorActorLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.SaveActorUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
  private final SaveActorUseCase saveActorUseCase;

  private GetActorDetailsUseCase.ActorDto actorDto;
  private String pageTitle;

  private ActorEditorActorLayout actorEditorActorLayout;

  public ActorEditorView(
      GetActorDetailsUseCase getActorDetailsUseCase, SaveActorUseCase saveActorUseCase) {
    this.getActorDetailsUseCase = getActorDetailsUseCase;
    this.saveActorUseCase = saveActorUseCase;
  }

  private void rebuildEditor() {
    removeAll();
    Tab actorTab = new Tab(VaadinIcon.USER.create(), new Span("Actor"));
    actorTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    Tab mindTab = new Tab(VaadinIcon.ABACUS.create(), new Span("Mind"));
    mindTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    Tab outfitsTab = new Tab(VaadinIcon.GLASSES.create(), new Span("Outfits"));
    outfitsTab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);

    actorEditorActorLayout = new ActorEditorActorLayout(actorDto);
    TabSheet tabSheet = new TabSheet();
    tabSheet.add(actorTab, actorEditorActorLayout);
    tabSheet.add(mindTab, new Div(new Text("This is the Mind tab content")));
    tabSheet.add(outfitsTab, new Div(new Text("This is the Outfits tab content")));

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveActorListener());

    add(tabSheet);
    add(saveButton);
  }

  private ComponentEventListener<ClickEvent<Button>> saveActorListener() {
    return (ClickEvent<Button> t) -> {
      UUID actorId =
          saveActorUseCase.execute(
              new SaveActorUseCase.Command(
                  actorDto.id(),
                  actorEditorActorLayout.getNameValue(),
                  actorEditorActorLayout.getPhysicalDescriptionValue()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actor/" + actorId));
      Notification notification =
          Notification.show(actorEditorActorLayout.getNameValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
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
    rebuildEditor();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
