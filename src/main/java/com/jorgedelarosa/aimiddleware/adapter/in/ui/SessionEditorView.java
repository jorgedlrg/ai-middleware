package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ByteImage;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.EditSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "sessions/:sessionId?", layout = MainView.class)
@RequiredArgsConstructor
public class SessionEditorView extends VerticalLayout
    implements HasDynamicTitle, BeforeEnterObserver {
  private final GetSessionDetailsUseCase getSessionDetailsUseCase;
  private final GetScenariosUseCase getScenariosUseCase;
  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;
  private final GetActorsUseCase getActorsUseCase;
  private final CreateSessionUseCase createSessionUseCase;
  private final EditSessionUseCase updateSessionUseCase;

  private String pageTitle;
  private UUID session = null;
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
    // lock components when editing an existing session
    if (session != null) {
      scenariosComboBox.setEnabled(false);
    }
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
      List<GetActorsUseCase.ActorDto> actors = getActorsUseCase.execute();
      for (GetScenarioDetailsUseCase.RoleDto role : scenarioDetails.roles()) {
        ComboBox<GetActorsUseCase.ActorDto> actorsComboBox =
            new ComboBox<>("Select actor for role " + role.name() + ":");
        actorsComboBox.setItems(actors);
        actorsComboBox.setItemLabelGenerator(GetActorsUseCase.ActorDto::name);
        actorsComboBox.setRenderer(actorComboRenderer);
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
        if (session != null) {
          intros.setEnabled(false);
        }
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
        createSession.addClickListener(saveSessionListener());
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
      if (selectedIntro != null && selectedIntro.performer().equals(role)) {
        selectedIntro = null;
      }
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

  private final Renderer<GetActorsUseCase.ActorDto> actorComboRenderer =
      new ComponentRenderer<>(
          actor -> {
            Image portrait;
            if (actor.portrait().length > 0) {
              portrait = new ByteImage("Portrait", actor.portrait());
              portrait.setMaxWidth("64px");
              portrait.setMinWidth("64px");
              return new Div(portrait, new Text(actor.name()));
            } else {
              Icon icon = LumoIcon.PHOTO.create();
              icon.getStyle()
                  .setColor("var(--lumo-primary-color)")
                  .setBackgroundColor("var(--lumo-primary-color-10pct)");
              icon.setSize("64px");
              return new VerticalLayout(icon, new Text(actor.name()));
            }
          });

  private ComponentEventListener<ClickEvent<Button>> saveSessionListener() {
    return (ClickEvent<Button> t) -> {
      if (session != null) {
        updateSessionUseCase.execute(
            new EditSessionUseCase.Command(
                session,
                selectedContext.id(),
                performances.values().stream()
                    .map(p -> new EditSessionUseCase.PerformanceDto(p.actor(), p.role()))
                    .toList(),
                selectedLocale));
        t.getSource().getUI().ifPresent(ui -> ui.navigate("sessions/" + session + "/interact"));
        Notification notification = Notification.show("Session " + session + " updated!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        UUID sessionId =
            createSessionUseCase.execute(
                new CreateSessionUseCase.Command(
                    selectedScenario.id(),
                    selectedContext.id(),
                    new ArrayList(performances.values()),
                    selectedLocale,
                    Optional.ofNullable(selectedIntro != null ? selectedIntro.id() : null)));
        t.getSource().getUI().ifPresent(ui -> ui.navigate("sessions/" + sessionId + "/interact"));
        Notification notification = Notification.show("Session " + sessionId + " created!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      }
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<UUID> sessionParam =
        event.getRouteParameters().get("sessionId").map(e -> UUID.fromString(e));

    if (sessionParam.isPresent()) {
      session = sessionParam.get();
      GetSessionDetailsUseCase.SessionDto dto =
          getSessionDetailsUseCase.execute(new GetSessionDetailsUseCase.Command(session));

      selectedScenario =
          getScenariosUseCase.execute(new GetScenariosUseCase.Command()).stream()
              .filter(e -> e.id().equals(dto.scenario()))
              .findFirst()
              .orElseThrow();
      GetScenarioDetailsUseCase.ScenarioDto scenarioDetails =
          getScenarioDetailsUseCase.execute(
              new GetScenarioDetailsUseCase.Command(selectedScenario.id()));
      selectedContext =
          scenarioDetails.contexts().stream()
              .filter(e -> e.id().equals(dto.currentContext()))
              .findFirst()
              .orElseThrow();

      dto.performances().stream()
          .forEach(
              p ->
                  performances.put(
                      p.role(), new CreateSessionUseCase.PerformanceDto(p.actor(), p.role())));

      selectedLocale = dto.locale();

      pageTitle = "Session - " + selectedScenario.name();
    } else {
      pageTitle = "New Session";
    }

    render();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
