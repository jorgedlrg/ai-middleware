package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route(value = "actors-list", layout = MainView.class)
@PageTitle("Actors")
public class ActorsView extends VerticalLayout {

  private final GetActorsUseCase getActorsUseCase;

  private final Grid<GetActorsUseCase.ActorDto> actorsGrid;

  public ActorsView(GetActorsUseCase getActorsUseCase) {
    this.getActorsUseCase = getActorsUseCase;

    actorsGrid = new Grid<>();
    actorsGrid.addColumn(GetActorsUseCase.ActorDto::id).setHeader("Id");
    actorsGrid.addColumn(GetActorsUseCase.ActorDto::name).setHeader("Name");
    actorsGrid.addItemClickListener(editActorListener());
    fillActorsGrid();

    add(actorsGrid);
    add(new Button("New Actor", newActorListener()));
  }

  private void fillActorsGrid() {
    actorsGrid.setItems(getActorsUseCase.execute(new GetActorsUseCase.Command()));
  }

  /**
   * Navigate to the clicked actor
   *
   * @return
   */
  private ComponentEventListener<ItemClickEvent<GetActorsUseCase.ActorDto>> editActorListener() {
    return (ItemClickEvent<GetActorsUseCase.ActorDto> t) -> {
      t.getColumn().getUI().ifPresent(ui -> ui.navigate("actors/" + t.getItem().id()));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> newActorListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors"));
    };
  }
}
