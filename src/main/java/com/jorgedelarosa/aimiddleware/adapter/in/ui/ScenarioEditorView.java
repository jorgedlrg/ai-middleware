package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.SaveScenarioUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import java.util.Collections;
import java.util.UUID;

/**
 * @author jorge
 */
@Route(value = "scenarios", layout = MainView.class)
public class ScenarioEditorView extends VerticalLayout
    implements HasDynamicTitle, HasUrlParameter<String> {

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final SaveScenarioUseCase saveScenarioUseCase;

  private String pageTitle;
  private GetScenarioDetailsUseCase.ScenarioDto scenarioDto;

  private TextField name;

  public ScenarioEditorView(
      GetScenarioDetailsUseCase getScenarioDetailsUseCase,
      SaveScenarioUseCase saveScenarioUseCase) {
    this.getScenarioDetailsUseCase = getScenarioDetailsUseCase;
    this.saveScenarioUseCase = saveScenarioUseCase;
  }

  private void rebuildEditor() {
    removeAll();

    name = new TextField("Name");
    name.setValue(scenarioDto.name());
    name.setRequired(true);

    Grid<GetScenarioDetailsUseCase.ContextDto> contextGrid =
        new Grid<>(GetScenarioDetailsUseCase.ContextDto.class, false);
    contextGrid.addColumn(GetScenarioDetailsUseCase.ContextDto::id).setHeader("id");
    contextGrid.addColumn(GetScenarioDetailsUseCase.ContextDto::name).setHeader("name");
    contextGrid.setItems(scenarioDto.contexts());
    contextGrid.addItemClickListener(editContextListener());

    Grid<GetScenarioDetailsUseCase.RoleDto> roleGrid =
        new Grid<>(GetScenarioDetailsUseCase.RoleDto.class, false);
    roleGrid.addColumn(GetScenarioDetailsUseCase.RoleDto::id).setHeader("id");
    roleGrid.addColumn(GetScenarioDetailsUseCase.RoleDto::name).setHeader("name");
    roleGrid.setItems(scenarioDto.roles());
    roleGrid.addItemClickListener(editRoleListener());

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveScenarioListener());

    Button addContext = new Button("Add new Context");
    addContext.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    addContext.addClickListener(addNewContextListener());

    Button addRole = new Button("Add new Role");
    addRole.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    addRole.addClickListener(addNewRoleListener());

    add(name);
    add(contextGrid);
    add(addContext);
    add(roleGrid);
    add(addRole);
    add(saveButton);
  }

  private ComponentEventListener<ItemClickEvent<GetScenarioDetailsUseCase.ContextDto>>
      editContextListener() {
    return (ItemClickEvent<GetScenarioDetailsUseCase.ContextDto> t) -> {
      t.getColumn()
          .getUI()
          .ifPresent(
              ui -> ui.navigate("scenarios/" + scenarioDto.id() + "/contexts/" + t.getItem().id()));
    };
  }

  private ComponentEventListener<ItemClickEvent<GetScenarioDetailsUseCase.RoleDto>>
      editRoleListener() {
    return (ItemClickEvent<GetScenarioDetailsUseCase.RoleDto> t) -> {
      t.getColumn()
          .getUI()
          .ifPresent(
              ui -> ui.navigate("scenarios/" + scenarioDto.id() + "/roles/" + t.getItem().id()));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> addNewContextListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource()
          .getUI()
          .ifPresent(ui -> ui.navigate("scenarios/" + scenarioDto.id() + "/contexts"));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> addNewRoleListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource()
          .getUI()
          .ifPresent(ui -> ui.navigate("scenarios/" + scenarioDto.id() + "/roles"));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> saveScenarioListener() {
    return (ClickEvent<Button> t) -> {
      UUID scenarioId =
          saveScenarioUseCase.execute(
              new SaveScenarioUseCase.Command(scenarioDto.id(), name.getValue()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenarioId));
      Notification notification = Notification.show(name.getValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    if (parameter != null) {
      scenarioDto =
          getScenarioDetailsUseCase.execute(
              new GetScenarioDetailsUseCase.Command(UUID.fromString(parameter)));
      pageTitle = "Scenario Editor - " + scenarioDto.name();
    } else {
      scenarioDto =
          new GetScenarioDetailsUseCase.ScenarioDto(
              null, "", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
      pageTitle = "Scenario Editor - new";
    }
    rebuildEditor();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
