package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.RetrieveSessionInteractionsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.UUID;

// FIXME This is the first time I use Vaadin (I'm not a frontend DEV) so, take it easy.

/**
 * @author jorge
 */
@Route("interaction")
public class InteractionView extends VerticalLayout {
  private final UUID SESSION = UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a");

  private final UserInteractUseCase userInteractUseCase;
  private final MachineInteractUseCase machineInteractUseCase;
  private final RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase;

  private final TextArea inputText = new TextArea();

  private final Grid<RetrieveSessionInteractionsUseCase.InteractionDto> interactionGrid;

  public InteractionView(
      UserInteractUseCase userInteractUseCase,
      MachineInteractUseCase machineInteractUseCase,
      RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase) {
    this.userInteractUseCase = userInteractUseCase;
    this.machineInteractUseCase = machineInteractUseCase;
    this.retrieveSessionInteractionsUseCase = retrieveSessionInteractionsUseCase;

    inputText.setValueChangeMode(ValueChangeMode.ON_CHANGE);

    Button interactButton = new Button("Send");
    interactButton.addClickListener(e -> userInteractListener());

    Button machineButton = new Button("Generate Machine Interaction");
    machineButton.addClickListener(e -> machineInteractListener());

    interactionGrid = new Grid<>();
    interactionGrid
        .addColumn(RetrieveSessionInteractionsUseCase.InteractionDto::timestamp)
        .setHeader("Timestamp")
        .setFlexGrow(1);
    interactionGrid
        .addColumn(RetrieveSessionInteractionsUseCase.InteractionDto::actor)
        .setHeader("Actor")
        .setFlexGrow(1);
    interactionGrid
        .addColumn(RetrieveSessionInteractionsUseCase.InteractionDto::spokenText)
        .setHeader("Text")
        .setFlexGrow(5);
    interactionGrid.setItems(new ArrayList<>());
    interactionGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    add(interactionGrid);
    add(new HorizontalLayout(inputText, interactButton));
    add(machineButton);
  }

  private void userInteractListener() {
    userInteractUseCase.execute(
        new UserInteractUseCase.Command(
            SESSION,
            UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"),
            inputText.getValue()));

    interactionGrid.setItems(
        retrieveSessionInteractionsUseCase.execute(
            new RetrieveSessionInteractionsUseCase.Command(SESSION)));
  }

  private void machineInteractListener() {
    machineInteractUseCase.execute(
        new MachineInteractUseCase.Command(
            SESSION, UUID.fromString("655cfb3d-c740-48d2-ab4f-51e391c4deaf")));

    interactionGrid.setItems(
        retrieveSessionInteractionsUseCase.execute(
            new RetrieveSessionInteractionsUseCase.Command(SESSION)));
  }
}
