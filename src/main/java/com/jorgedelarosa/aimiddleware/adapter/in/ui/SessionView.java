package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteInteractionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.RetrieveSessionInteractionsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UpdateSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.UserInteractUseCase;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
@Route(value = "sessions/:sessionId?", layout = MainView.class)
public class SessionView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {
  private final UserInteractUseCase userInteractUseCase;
  private final MachineInteractUseCase machineInteractUseCase;
  private final RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase;
  private final UpdateSessionUseCase updateSessionUseCase;
  private final DeleteInteractionUseCase deleteInteractionUseCase;

  private UUID session;
  private String pageTitle;
  private final VirtualList<RetrieveSessionInteractionsUseCase.InteractionDto> interactionList;

  public SessionView(
      UserInteractUseCase userInteractUseCase,
      MachineInteractUseCase machineInteractUseCase,
      RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase,
      UpdateSessionUseCase updateSessionUseCase,
      DeleteInteractionUseCase deleteInteractionUseCase) {
    this.userInteractUseCase = userInteractUseCase;
    this.machineInteractUseCase = machineInteractUseCase;
    this.retrieveSessionInteractionsUseCase = retrieveSessionInteractionsUseCase;
    this.updateSessionUseCase = updateSessionUseCase;
    this.deleteInteractionUseCase = deleteInteractionUseCase;

    MessageInput input =
        new MessageInput(
            submitEvent -> {
              userInteractListener(submitEvent);
            });
    input.setWidthFull();

    Button machineButton = new Button("Generate Machine Interaction");
    machineButton.addClickListener(e -> machineInteractListener());

    interactionList = new VirtualList<>();
    interactionList.setRenderer(interactionRenderer);

    ComboBox<Locale> localeComboBox = new ComboBox<>("Answer language");
    localeComboBox.setItems(Locale.ENGLISH, Locale.CHINESE, Locale.forLanguageTag("es"));
    localeComboBox.setItemLabelGenerator(Locale::getDisplayLanguage);
    localeComboBox.setValue(
        Locale.forLanguageTag(
            "es")); // TODO: instead of 'retrieve interactions', retrieve the full session and REDO
    // this view
    localeComboBox.addValueChangeListener(e -> changeLocaleListener(e.getValue()));

    add(interactionList);
    add(input);
    add(machineButton);
    add(localeComboBox);
  }

  private void userInteractListener(MessageInput.SubmitEvent submitEvent) {
    userInteractUseCase.execute(
        new UserInteractUseCase.Command(
            session,
            UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"),
            submitEvent.getValue()));

    fillInteractionList();
  }

  private void machineInteractListener() {
    machineInteractUseCase.execute(
        new MachineInteractUseCase.Command(
            session, UUID.fromString("655cfb3d-c740-48d2-ab4f-51e391c4deaf")));

    fillInteractionList();
  }

  private void changeLocaleListener(Locale locale) {
    updateSessionUseCase.execute(new UpdateSessionUseCase.Command(session, locale));
  }

  private void deleteInteractionListener(UUID id) {
    deleteInteractionUseCase.execute(new DeleteInteractionUseCase.Command(session, id));
    fillInteractionList();
  }

  private void fillInteractionList() {
    interactionList.setItems(
        retrieveSessionInteractionsUseCase.execute(
            new RetrieveSessionInteractionsUseCase.Command(session)));
    interactionList.scrollToEnd();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    session = UUID.fromString(event.getRouteParameters().get("sessionId").orElseThrow());
    pageTitle = "Session - " + session;

    fillInteractionList();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }

  private final ComponentRenderer<Component, RetrieveSessionInteractionsUseCase.InteractionDto>
      interactionRenderer =
          new ComponentRenderer<>(
              interaction -> {
                HorizontalLayout interactionLayout = new HorizontalLayout();
                interactionLayout.setMargin(true);

                Avatar avatar = new Avatar(interaction.actorName());
                avatar.setHeight("64px");
                avatar.setWidth("64px");
                avatar.setColorIndex(Math.abs(interaction.actorName().hashCode()) % 5);

                VerticalLayout messageLayout = new VerticalLayout();
                messageLayout.setSpacing(false);
                messageLayout.setPadding(false);
                messageLayout
                    .getElement()
                    .appendChild(
                        ElementFactory.createStrong(interaction.actorName()),
                        ElementFactory.createLabel(interaction.timestamp().toString()));
                messageLayout.add(new Div(new Text(interaction.spokenText())));

                HorizontalLayout operationsLayout = new HorizontalLayout();
                operationsLayout.setSpacing(false);
                operationsLayout.setPadding(false);

                Icon deleteIcon = LumoIcon.CROSS.create();
                deleteIcon.setColor("red");
                Button deleteButton = new Button(deleteIcon);
                deleteButton.addClickListener(e -> deleteInteractionListener(interaction.id()));

                operationsLayout.add(deleteButton);

                interactionLayout.add(avatar, messageLayout, operationsLayout);
                return interactionLayout;
              });
}
