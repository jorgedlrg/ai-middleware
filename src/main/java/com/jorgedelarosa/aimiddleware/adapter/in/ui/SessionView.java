package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ByteImage;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.InteractionLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.NextInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.PreviousInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UpdateSessionContextUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UpdateSessionLocaleUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UserInteractUseCase;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
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
import com.vaadin.flow.theme.lumo.LumoIcon;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "sessions/:sessionId?", layout = MainView.class)
@RequiredArgsConstructor
public class SessionView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {
  private final UserInteractUseCase userInteractUseCase;
  private final MachineInteractUseCase machineInteractUseCase;
  private final DeleteInteractionUseCase deleteInteractionUseCase;

  private final UpdateSessionLocaleUseCase updateSessionLocaleUseCase;
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
  private RadioButtonGroup<GetSessionDetailsUseCase.PerformanceDto> radioGroup;

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

    radioGroup = new RadioButtonGroup<>();
    radioGroup.setLabel("You're:");
    radioGroup.setRenderer(performancesRenderer);
    radioGroup.setItems(sessionDetails.performances());
    radioGroup.setValue(sessionDetails.performances().getFirst());

    interactionList = new VirtualList<>();
    interactionList.setRenderer(interactionRenderer);
    interactionList.setItems(sessionDetails.interactions());
    interactionList.scrollToEnd();
    interactionList.setWidthFull();

    MessageInput input =
        new MessageInput(
            submitEvent -> {
              userInteractListener(submitEvent);
            });
    input.setWidthFull();

    HorizontalLayout generatePanel = new HorizontalLayout();
    for (GetSessionDetailsUseCase.PerformanceDto dto : sessionDetails.performances()) {
      Button machineButton = new Button(dto.actorName());
      machineButton.addClickListener(e -> machineInteractListener(dto.role()));
      generatePanel.add(machineButton);
    }

    ComboBox<Locale> localeComboBox = new ComboBox<>("Answer language");
    localeComboBox.setItems(Locale.ENGLISH, Locale.CHINESE, Locale.forLanguageTag("es"));
    localeComboBox.setItemLabelGenerator(Locale::getDisplayLanguage);
    localeComboBox.setValue(sessionDetails.locale());
    localeComboBox.addValueChangeListener(e -> changeLocaleListener(e.getValue()));

    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", session.toString(), deleteSessionListener());

    HorizontalLayout threezoneLayout = new HorizontalLayout();
    threezoneLayout.setPadding(true);
    threezoneLayout.setSizeFull();
    VerticalLayout left = new VerticalLayout();
    left.setWidth("20%");
    threezoneLayout.addToStart(left);
    VerticalLayout right = new VerticalLayout();
    right.setWidth("20%");
    threezoneLayout.addToEnd(right);
    for (int i = 0; i < sessionDetails.performances().size(); ++i) {
      Component portrait;
      if (sessionDetails.performances().get(i).portrait().length > 0) {
        portrait = new ByteImage("Portrait", sessionDetails.performances().get(i).portrait());
        ((ByteImage) portrait).setWidth("330px");
      } else {
        portrait = LumoIcon.PHOTO.create();
        portrait
            .getStyle()
            .setColor("var(--lumo-primary-color)")
            .setBackgroundColor("var(--lumo-primary-color-10pct)");
        ((Icon) portrait).setSize("330px");
      }
      if (i % 2 == 0) {
        left.add(portrait);
      } else {
        right.add(portrait);
      }
    }

    VerticalLayout middle = new VerticalLayout();
    middle.setWidth("60%");
    middle.add(contextComboBox);
    middle.add(radioGroup);
    middle.add(interactionList);
    middle.add(input);
    middle.add(generatePanel);
    middle.add(localeComboBox);
    middle.add(deleteButton);
    threezoneLayout.addToMiddle(middle);
    add(threezoneLayout);
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
            session, radioGroup.getValue().role(), submitEvent.getValue()));

    reloadInteractions();
  }

  private void machineInteractListener(UUID role) {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command(session, role));

    reloadInteractions();
  }

  private void changeLocaleListener(Locale locale) {
    updateSessionLocaleUseCase.execute(new UpdateSessionLocaleUseCase.Command(session, locale));
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
                InteractionLayout interactionLayout =  new InteractionLayout(
                    interaction, prevListener, nextListener, deleteListener);
                interactionLayout.setWidthFull();
                return interactionLayout;
              });
}
