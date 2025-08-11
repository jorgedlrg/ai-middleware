package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.DeleteIntroductionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.SaveIntroductionUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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
@Route(value = "scenarios/:scenarioId?/introductions/:introductionId?", layout = MainView.class)
@RequiredArgsConstructor
public class IntroductionEditorView extends VerticalLayout
    implements BeforeEnterObserver, HasDynamicTitle {

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final SaveIntroductionUseCase saveIntroductionUseCase;
  private final DeleteIntroductionUseCase deleteIntroductionUseCase;

  private String pageTitle;

  private GetScenarioDetailsUseCase.ScenarioDto scenarioDto;
  private GetScenarioDetailsUseCase.IntroductionDto dto = null;
  private UUID scenario;
  private UUID introduction;

  private TextArea spoken;
  private TextArea thoughts;
  private TextArea action;
  private ComboBox<GetScenarioDetailsUseCase.RoleDto> roles;
  private ComboBox<GetScenarioDetailsUseCase.ContextDto> contexts;

  private void render() {
    removeAll();
    spoken = new TextArea("Spoken text");
    spoken.setMinRows(4);
    thoughts = new TextArea("Thoughts");
    thoughts.setMinRows(4);
    action = new TextArea("Action");
    action.setMinRows(4);
    roles = new ComboBox<>("Performer");
    roles.setItems(scenarioDto.roles());
    roles.setItemLabelGenerator(GetScenarioDetailsUseCase.RoleDto::name);
    roles.setRequiredIndicatorVisible(true);
    contexts = new ComboBox<>("Context");
    contexts.setItems(scenarioDto.contexts());
    contexts.setItemLabelGenerator(GetScenarioDetailsUseCase.ContextDto::name);
    contexts.setRequiredIndicatorVisible(true);
    if (dto != null) {
      spoken.setValue(dto.spokenText());
      thoughts.setValue(dto.thoughtText().orElse(""));
      action.setValue(dto.actionText().orElse(""));
      roles.setValue(
          scenarioDto.roles().stream()
              .filter(e -> e.id().equals(dto.performer()))
              .findFirst()
              .orElseThrow());
      contexts.setValue(
          scenarioDto.contexts().stream()
              .filter(e -> e.id().equals(dto.context()))
              .findFirst()
              .orElseThrow());
    }

    FormLayout formLayout = new FormLayout();
    formLayout.add(thoughts, 3);
    formLayout.add(action, 3);
    formLayout.add(spoken, 3);
    formLayout.add(roles, 1);
    formLayout.add(contexts, 1);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveIntroListener());

    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", "introduction", deleteIntroListener());

    add(formLayout);
    add(new Div(saveButton, deleteButton));
  }

  private ComponentEventListener<ClickEvent<Button>> saveIntroListener() {
    return (ClickEvent<Button> t) -> {
      String sth = null;
      if (!thoughts.getValue().equals("")) {
        sth = thoughts.getValue();
      }
      String sact = null;
      if (!action.getValue().equals("")) {
        sact = action.getValue();
      }
      saveIntroductionUseCase.execute(
          new SaveIntroductionUseCase.Command(
              scenario,
              introduction,
              spoken.getValue(),
              Optional.ofNullable(sth),
              Optional.ofNullable(sact),
              roles.getValue().id(),
              contexts.getValue().id()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenario));
      Notification notification = Notification.show("Introduction saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteIntroListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteIntroductionUseCase.execute(
          new DeleteIntroductionUseCase.Command(scenario, introduction));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("scenarios/" + scenario));
      Notification notification = Notification.show("Introduction deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    scenario = UUID.fromString(event.getRouteParameters().get("scenarioId").orElseThrow());
    scenarioDto =
        getScenarioDetailsUseCase.execute(new GetScenarioDetailsUseCase.Command(scenario));

    introduction =
        event.getRouteParameters().get("introductionId").map(e -> UUID.fromString(e)).orElse(null);
    if (introduction != null) {
      dto =
          scenarioDto.introductions().stream()
              .filter(e -> e.id().equals(introduction))
              .findFirst()
              .orElseThrow();
      pageTitle = "Introduction Editor";
    } else {
      pageTitle = "Introduction Editor - new";
    }

    render();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
