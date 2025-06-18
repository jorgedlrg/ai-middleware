package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.RetrieveSessionInteractionsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.Instant;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Route(value = "interaction", layout = MainView.class)
@PageTitle("Interaction")
public class InteractionView extends VerticalLayout implements HasUrlParameter<String> {
  private UUID session;

  private final UserInteractUseCase userInteractUseCase;
  private final MachineInteractUseCase machineInteractUseCase;
  private final RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase;

  private final MessageList interationList;

  public InteractionView(
      UserInteractUseCase userInteractUseCase,
      MachineInteractUseCase machineInteractUseCase,
      RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase) {
    this.userInteractUseCase = userInteractUseCase;
    this.machineInteractUseCase = machineInteractUseCase;
    this.retrieveSessionInteractionsUseCase = retrieveSessionInteractionsUseCase;

    MessageInput input =
        new MessageInput(
            submitEvent -> {
              userInteractListener(submitEvent);
            });
    input.setWidthFull();

    Button machineButton = new Button("Generate Machine Interaction");
    machineButton.addClickListener(e -> machineInteractListener());

    interationList = new MessageList();

    add(interationList);
    add(input);
    add(machineButton);
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

  private void fillInteractionList() {
    interationList.setItems(
        retrieveSessionInteractionsUseCase
            .execute(new RetrieveSessionInteractionsUseCase.Command(session))
            .stream()
            .map(e -> MessageMapper.INSTANCE.toMessage(e))
            .toList());
  }

  @Override
  public void setParameter(BeforeEvent be, String t) {
    session = UUID.fromString(t);
    fillInteractionList();
  }

  @Mapper
  public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    default MessageListItem toMessage(RetrieveSessionInteractionsUseCase.InteractionDto dto) {
      MessageListItem item =
          new MessageListItem(dto.spokenText(), Instant.now(), dto.actor().toString());
      // FIXME Max 5 different colors
      item.setUserColorIndex(Math.abs(dto.actor().hashCode()) % 5);
      return item;
    }
  }
}
