package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ScenarioEditorScenarioLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.SaveScenarioUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
@Route(value = "scenario", layout = MainView.class)
public class ScenarioEditorView extends VerticalLayout
    implements HasDynamicTitle, HasUrlParameter<String> {

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final SaveScenarioUseCase saveScenarioUseCase;

  private String pageTitle;
  private GetScenarioDetailsUseCase.ScenarioDto scenarioDto;
  private ScenarioEditorScenarioLayout scenarioEditorScenarioLayout;

  public ScenarioEditorView(
      GetScenarioDetailsUseCase getScenarioDetailsUseCase,
      SaveScenarioUseCase saveScenarioUseCase) {
    this.getScenarioDetailsUseCase = getScenarioDetailsUseCase;
    this.saveScenarioUseCase = saveScenarioUseCase;
  }

  private void rebuildEditor() {
    removeAll();

    scenarioEditorScenarioLayout = new ScenarioEditorScenarioLayout(scenarioDto);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveScenarioListener());

    add(scenarioEditorScenarioLayout);
    add(saveButton);
  }

  private ComponentEventListener<ClickEvent<Button>> saveScenarioListener() {
    return (ClickEvent<Button> t) -> {
      UUID scenarioId =
          saveScenarioUseCase.execute(
              new SaveScenarioUseCase.Command(
                  scenarioDto.id(),
                  scenarioEditorScenarioLayout.getNameValue(),
                  Collections.EMPTY_LIST,
                  Collections.EMPTY_LIST));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenario/" + scenarioId));
      Notification notification =
          Notification.show(scenarioEditorScenarioLayout.getNameValue() + " saved!");
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
