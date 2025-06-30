package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.DeleteRoleUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.SaveRoleUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
@Route(value = "scenarios/:scenarioId?/roles/:roleId?", layout = MainView.class)
@RequiredArgsConstructor
public class RoleEditorView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final SaveRoleUseCase saveRoleUseCase;
  private final DeleteRoleUseCase deleteRoleUseCase;

  private String pageTitle;

  private UUID scenario;
  private UUID role;
  private TextField name;
  private TextArea details;

  private void render() {
    removeAll();
    name = new TextField("Name");
    details = new TextArea("Details");
    if (role != null) {
      GetScenarioDetailsUseCase.RoleDto dto = retrieveRole();
      name.setValue(dto.name());
      details.setValue(dto.details());
    }

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 1);
    formLayout.add(details, 2);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveRoleListener());

    Button deleteButton = new Button("Delete");
    deleteButton.addThemeVariants(ButtonVariant.LUMO_WARNING);
    deleteButton.addClickListener(deleteRoleListener());

    add(formLayout);
    add(new Div(saveButton, deleteButton));
  }

  private ComponentEventListener<ClickEvent<Button>> saveRoleListener() {
    return (ClickEvent<Button> t) -> {
      UUID roleId =
          saveRoleUseCase.execute(
              new SaveRoleUseCase.Command(scenario, role, name.getValue(), details.getValue()));
      t.getSource()
          .getUI()
          .ifPresent(ui -> ui.navigate("scenarios/" + scenario + "/roles/" + roleId));
      Notification notification = Notification.show(name.getValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  private ComponentEventListener<ClickEvent<Button>> deleteRoleListener() {
    return (ClickEvent<Button> t) -> {
      deleteRoleUseCase.execute(new DeleteRoleUseCase.Command(scenario, role));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenario));
      Notification notification = Notification.show(name.getValue() + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    scenario = UUID.fromString(event.getRouteParameters().get("scenarioId").orElseThrow());

    Optional<String> sc = event.getRouteParameters().get("roleId");
    sc.ifPresent(e -> role = UUID.fromString(e));
    if (role != null) {
      GetScenarioDetailsUseCase.RoleDto dto = retrieveRole();
      pageTitle = "Role Editor - " + dto.name();
    } else {
      pageTitle = "Role Editor - new";
    }

    render();
  }

  private GetScenarioDetailsUseCase.RoleDto retrieveRole() {
    GetScenarioDetailsUseCase.ScenarioDto sdto =
        getScenarioDetailsUseCase.execute(new GetScenarioDetailsUseCase.Command(scenario));

    return sdto.roles().stream()
        .filter(e -> e.id().equals(role))
        .findFirst()
        .orElseThrow(); // FIXME handle this properly
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
