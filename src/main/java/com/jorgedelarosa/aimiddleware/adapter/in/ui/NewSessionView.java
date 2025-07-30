package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "new-session", layout = MainView.class)
@RequiredArgsConstructor
@PageTitle("New Session")
public class NewSessionView extends VerticalLayout implements BeforeEnterObserver {
  private final GetScenariosUseCase getScenariosUseCase;
  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final GetActorsUseCase getActorsUseCase;
  private final CreateSessionUseCase createSessionUseCase;

  private GetScenariosUseCase.ScenarioDto selectedScenario = null;
  private GetScenarioDetailsUseCase.ContextDto selectedContext = null;
  private Map<UUID, CreateSessionUseCase.PerformanceDto> performances = new HashMap<>();
  private GetScenarioDetailsUseCase.IntroductionDto selectedIntro = null;
  private Locale selectedLocale = null;

  private void render() {
    removeAll();

    ComboBox<GetScenariosUseCase.ScenarioDto> scenariosComboBox = new ComboBox<>("Scenario:");
    scenariosComboBox.setRequiredIndicatorVisible(true);
    scenariosComboBox.setItems(getScenariosUseCase.execute(new GetScenariosUseCase.Command()));
    scenariosComboBox.setItemLabelGenerator(GetScenariosUseCase.ScenarioDto::name);
    if (selectedScenario != null) {
      // Set value before listener, otherwise it loops
      scenariosComboBox.setValue(selectedScenario);
    }
    scenariosComboBox.addValueChangeListener(e -> selectScenarioListener(e.getValue()));

    add(scenariosComboBox);
    if (selectedScenario != null) {
      GetScenarioDetailsUseCase.ScenarioDto scenarioDetails =
          getScenarioDetailsUseCase.execute(
              new GetScenarioDetailsUseCase.Command(selectedScenario.id()));

      // Current context
      ComboBox<GetScenarioDetailsUseCase.ContextDto> contextsComboBox = new ComboBox<>("Context:");
      contextsComboBox.setRequiredIndicatorVisible(true);
      contextsComboBox.setItems(scenarioDetails.contexts());
      contextsComboBox.setItemLabelGenerator(GetScenarioDetailsUseCase.ContextDto::name);
      if (selectedContext != null) {
        contextsComboBox.setValue(selectedContext);
      }
      contextsComboBox.addValueChangeListener(e -> selectContextListener(e.getValue()));

      add(contextsComboBox);

      // Performances
      List<GetActorsUseCase.ActorDto> actors =
          getActorsUseCase.execute(new GetActorsUseCase.Command());
      for (GetScenarioDetailsUseCase.RoleDto role : scenarioDetails.roles()) {
        ComboBox<GetActorsUseCase.ActorDto> actorsComboBox =
            new ComboBox<>("Select actor for role " + role.name() + ":");
        actorsComboBox.setItems(actors);
        actorsComboBox.setItemLabelGenerator(GetActorsUseCase.ActorDto::name);
        actorsComboBox.setClearButtonVisible(true);
        // If the role already has an actor assigned, set it to the combobox
        performances.keySet().stream()
            .filter(e -> e.equals(role.id()))
            .findFirst()
            .ifPresent(
                r ->
                    actorsComboBox.setValue(
                        actors.stream()
                            .filter(a -> a.id().equals(performances.get(r).actor()))
                            .findFirst()
                            .orElseThrow()));
        actorsComboBox.addValueChangeListener(e -> selectActorListener(e.getValue(), role.id()));
        add(actorsComboBox);
      }
      if (selectedContext != null) {
        ComboBox<GetScenarioDetailsUseCase.IntroductionDto> intros =
            new ComboBox<>("Select session intro:");
        intros.setItemLabelGenerator(GetScenarioDetailsUseCase.IntroductionDto::spokenText);
        // filter intros shown by context and roles in use
        intros.setItems(
            scenarioDetails.introductions().stream()
                .filter(i -> i.context().equals(selectedContext.id()))
                .filter(i -> performances.get(i.performer()) != null)
                .toList());
        if (selectedIntro != null) {
          intros.setValue(selectedIntro);
        }
        intros.addValueChangeListener(e -> selectIntroListener(e.getValue()));
        add(intros);
      }

      ComboBox<Locale> localeComboBox = new ComboBox<>("Session language:");
      localeComboBox.setRequiredIndicatorVisible(true);
      localeComboBox.setItems(Locale.ENGLISH, Locale.CHINESE, Locale.forLanguageTag("es"));
      localeComboBox.setItemLabelGenerator(Locale::getDisplayLanguage);
      if (selectedLocale != null) {
        localeComboBox.setValue(selectedLocale);
      }
      localeComboBox.addValueChangeListener(e -> selectLocaleListener(e.getValue()));
      add(localeComboBox);

      if (selectedContext != null && selectedLocale != null && performances.size() > 1) {
        Button createSession = new Button("Save");
        createSession.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createSession.addClickListener(createSessionListener());
        add(createSession);
      }
    }
  }

  private void selectScenarioListener(GetScenariosUseCase.ScenarioDto dto) {
    selectedScenario = dto;
    selectedContext = null;
    performances = new HashMap<>();
    selectedLocale = null;

    render();
  }

  private void selectContextListener(GetScenarioDetailsUseCase.ContextDto dto) {
    selectedContext = dto;

    render();
  }

  private void selectActorListener(GetActorsUseCase.ActorDto dto, UUID role) {
    if (dto != null) {
      performances.put(role, new CreateSessionUseCase.PerformanceDto(dto.id(), role));
    } else {
      performances.remove(role);
    }

    render();
  }

  private void selectIntroListener(GetScenarioDetailsUseCase.IntroductionDto dto) {
    selectedIntro = dto;

    render();
  }

  private void selectLocaleListener(Locale locale) {
    selectedLocale = locale;

    render();
  }

  private ComponentEventListener<ClickEvent<Button>> createSessionListener() {
    return (ClickEvent<Button> t) -> {
      UUID sessionId =
          createSessionUseCase.execute(
              new CreateSessionUseCase.Command(
                  selectedScenario.id(),
                  selectedContext.id(),
                  new ArrayList(performances.values()),
                  selectedLocale));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("sessions/" + sessionId + "/interact"));
      Notification notification = Notification.show("Session " + sessionId + " created!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
