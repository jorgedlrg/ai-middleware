package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.InteractionLayout;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.PerformanceCard;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.NextInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.PreviousInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UpdateSessionContextUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UserInteractUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "sessions/:sessionId?/interact", layout = MainView.class)
@RequiredArgsConstructor
public class SessionView extends HorizontalLayout implements HasDynamicTitle, BeforeEnterObserver {
  private final UserInteractUseCase userInteractUseCase;
  private final MachineInteractUseCase machineInteractUseCase;
  private final DeleteInteractionUseCase deleteInteractionUseCase;

  private final GetSessionDetailsUseCase getSessionDetailsUseCase;
  private final DeleteSessionUseCase deleteSessionUseCase;
  private final UpdateSessionContextUseCase updateSessionContextUseCase;
  private final PreviousInteractionUseCase previousInteractionUseCase;
  private final NextInteractionUseCase nextInteractionUseCase;

  private final GetScenarioDetailsUseCase getScenarioDetailsUseCase;

  private GetSessionDetailsUseCase.SessionDto sessionDetails;
  private GetScenarioDetailsUseCase.ScenarioDto scenarioDetails;

  private UUID session;
  private String pageTitle;
  private VirtualList<GetSessionDetailsUseCase.InteractionDto> interactionList;
  private RadioButtonGroup<GetSessionDetailsUseCase.PerformanceDto> userActorSelector;
  private RadioButtonGroup<GetSessionDetailsUseCase.PerformanceDto> autoreplySelector;

  private void render() {
    removeAll();

    sessionDetails =
        getSessionDetailsUseCase.execute(new GetSessionDetailsUseCase.Command(session));
    scenarioDetails =
        getScenarioDetailsUseCase.execute(
            new GetScenarioDetailsUseCase.Command(sessionDetails.scenario()));

    ComboBox<GetScenarioDetailsUseCase.ContextDto> contextComboBox =
        new ComboBox<>("Current context");
    contextComboBox.setItems(scenarioDetails.contexts());
    contextComboBox.setItemLabelGenerator(GetScenarioDetailsUseCase.ContextDto::name);
    contextComboBox.setValue(
        scenarioDetails.contexts().stream()
            .filter(e -> e.id().equals(sessionDetails.currentContext()))
            .findFirst()
            .orElseThrow());
    contextComboBox.addValueChangeListener(e -> changeContextListener(e.getValue()));

    autoreplySelector = new RadioButtonGroup<>("Auto reply:");
    autoreplySelector.setRenderer(performancesRenderer);
    List<GetSessionDetailsUseCase.PerformanceDto> autoreplyPerformances = new ArrayList();
    GetSessionDetailsUseCase.PerformanceDto disabled =
        new GetSessionDetailsUseCase.PerformanceDto(null, null, "Disabled", "No auto reply", null);
    autoreplyPerformances.add(disabled);
    autoreplyPerformances.addAll(sessionDetails.performances());
    autoreplySelector.setItems(autoreplyPerformances);
    autoreplySelector.setValue(disabled);

    userActorSelector = new RadioButtonGroup<>("You're:");
    userActorSelector.setRenderer(performancesRenderer);
    userActorSelector.setItems(sessionDetails.performances());
    userActorSelector.setValue(sessionDetails.performances().getFirst());

    interactionList = new VirtualList<>();
    interactionList.setRenderer(interactionRenderer);
    interactionList.setItems(sessionDetails.interactions());
    interactionList.scrollToEnd();
    interactionList.addClassNames(
        LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE, LumoUtility.Background.BASE);

    MessageInput input =
        new MessageInput(
            submitEvent -> {
              userInteractListener(submitEvent);
            });
    input.setWidthFull();

    Button editSession = new Button("Edit session");
    editSession.addClickListener(editSessionListener());
    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", session.toString(), deleteSessionListener());

    setPadding(false);
    setSpacing(false);

    VerticalLayout left = new VerticalLayout();
    left.setWidth("20%");
    left.addClassNames(LumoUtility.Overflow.AUTO);
    VerticalLayout right = new VerticalLayout();
    right.setWidth("20%");
    right.addClassNames(LumoUtility.Overflow.AUTO);

    for (int i = 0; i < sessionDetails.performances().size(); ++i) {
      Card performanceCard =
          new PerformanceCard(
              sessionDetails.performances().get(i), (id) -> machineInteractListener(id));
      if (i % 2 == 0) {
        left.add(performanceCard);
      } else {
        right.add(performanceCard);
      }
    }

    VerticalLayout middle = new VerticalLayout();
    middle.setWidth("60%");
    middle.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.EVENLY);
    middle.add(interactionList);
    middle.add(autoreplySelector);
    middle.add(userActorSelector);
    middle.add(input);
    middle.add(contextComboBox);
    middle.add(editSession);
    middle.add(deleteButton);

    add(left);
    add(middle);
    add(right);
    addClassNames(
        LumoUtility.Display.FLEX, LumoUtility.JustifyContent.EVENLY, LumoUtility.Height.FULL);
  }

  private void reloadInteractions() {
    sessionDetails =
        getSessionDetailsUseCase.execute(new GetSessionDetailsUseCase.Command(session));
    interactionList.setItems(sessionDetails.interactions());
    interactionList.scrollToEnd();
  }

  private void userInteractListener(MessageInput.SubmitEvent submitEvent) {
    userInteractUseCase.execute(
        new UserInteractUseCase.Command(
            session, userActorSelector.getValue().role(), submitEvent.getValue()));

    reloadInteractions();
  }

  private void machineInteractListener(UUID role) {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command(session, role));

    reloadInteractions();
  }

  private void changeContextListener(GetScenarioDetailsUseCase.ContextDto context) {
    updateSessionContextUseCase.execute(
        new UpdateSessionContextUseCase.Command(session, context.id()));
  }

  private void previousInteractionListener(UUID id) {
    previousInteractionUseCase.execute(new PreviousInteractionUseCase.Command(session));
    reloadInteractions();
  }

  private void nextInteractionListener(UUID id) {
    nextInteractionUseCase.execute(new NextInteractionUseCase.Command(session));
    reloadInteractions();
  }

  private void deleteInteractionListener(UUID id) {
    deleteInteractionUseCase.execute(new DeleteInteractionUseCase.Command(session, id));
    reloadInteractions();
  }

  private ComponentEventListener<ClickEvent<Button>> editSessionListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("sessions/" + session));
    };
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteSessionListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteSessionUseCase.execute(new DeleteSessionUseCase.Command(session));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("sessions-list"));
      Notification notification = Notification.show(session + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    session = UUID.fromString(event.getRouteParameters().get("sessionId").orElseThrow());

    render();
    pageTitle = "Session - " + scenarioDetails.name();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }

  private final ComponentRenderer<Component, GetSessionDetailsUseCase.PerformanceDto>
      performancesRenderer =
          new ComponentRenderer<>(
              performance -> {
                HorizontalLayout performanceLayour = new HorizontalLayout();
                performanceLayour.add(
                    new Span(performance.actorName() + " (" + performance.roleName() + ")"));
                return performanceLayour;
              });

  private final ComponentRenderer<Component, GetSessionDetailsUseCase.InteractionDto>
      interactionRenderer =
          new ComponentRenderer<>(
              interaction -> {
                InteractionLayout.OneUuidVoidOperator prevListener =
                    (id) -> previousInteractionListener(id);
                InteractionLayout.OneUuidVoidOperator nextListener =
                    (id) -> nextInteractionListener(id);
                InteractionLayout.OneUuidVoidOperator deleteListener =
                    (id) -> deleteInteractionListener(id);
                InteractionLayout interactionLayout =
                    new InteractionLayout(interaction, prevListener, nextListener, deleteListener);
                return interactionLayout;
              });
}
