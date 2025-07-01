package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.DeleteContextUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.SaveContextUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "scenarios/:scenarioId?/contexts/:contextId?", layout = MainView.class)
@RequiredArgsConstructor
public class ContextEditorView extends VerticalLayout
    implements BeforeEnterObserver, HasDynamicTitle {

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final SaveContextUseCase saveContextUseCase;
  private final DeleteContextUseCase deleteContextUseCase;

  private String pageTitle;

  private UUID scenario;
  private UUID context;
  private TextField name;
  private TextArea physicalDescription;

  private void render() {
    removeAll();
    name = new TextField("Name");
    physicalDescription = new TextArea("Physical description");
    if (context != null) {
      GetScenarioDetailsUseCase.ContextDto dto = retrieveContext();
      name.setValue(dto.name());
      physicalDescription.setValue(dto.physicalDescription());
    }

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 1);
    formLayout.add(physicalDescription, 2);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveContextListener());

    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", name.getValue(), deleteContextListener());

    add(formLayout);
    add(new Div(saveButton, deleteButton));
  }

  private ComponentEventListener<ClickEvent<Button>> saveContextListener() {
    return (ClickEvent<Button> t) -> {
      saveContextUseCase.execute(
          new SaveContextUseCase.Command(
              scenario, context, name.getValue(), physicalDescription.getValue()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenario));
      Notification notification = Notification.show(name.getValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteContextListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteContextUseCase.execute(new DeleteContextUseCase.Command(scenario, context));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenario));
      Notification notification = Notification.show(name.getValue() + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    scenario = UUID.fromString(event.getRouteParameters().get("scenarioId").orElseThrow());

    Optional<String> sc = event.getRouteParameters().get("contextId");
    sc.ifPresent(e -> context = UUID.fromString(e));
    if (context != null) {
      GetScenarioDetailsUseCase.ContextDto dto = retrieveContext();
      pageTitle = "Context Editor - " + dto.name();
    } else {
      pageTitle = "Context Editor - new";
    }

    render();
  }

  private GetScenarioDetailsUseCase.ContextDto retrieveContext() {
    GetScenarioDetailsUseCase.ScenarioDto sdto =
        getScenarioDetailsUseCase.execute(new GetScenarioDetailsUseCase.Command(scenario));

    return sdto.contexts().stream()
        .filter(e -> e.id().equals(context))
        .findFirst()
        .orElseThrow(); // FIXME handle this properly
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
