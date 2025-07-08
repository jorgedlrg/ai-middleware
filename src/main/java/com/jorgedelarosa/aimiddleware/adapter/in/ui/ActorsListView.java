package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorCard;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase.ActorDto;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "actors-list", layout = MainView.class)
@PageTitle("Actors")
@RequiredArgsConstructor
public class ActorsListView extends VerticalLayout implements BeforeEnterObserver {

  private final GetActorsUseCase getActorsUseCase;
  private final DeleteActorUseCase deleteActorUseCase;
  private static final int GRID_WIDTH = 3;

  private void render() {
    removeAll();

    List<ActorDto> dtos = getActorsUseCase.execute(new GetActorsUseCase.Command());
    List<ActorCard> cards = dtos.stream().map(e -> new ActorCard(e, deleteActorUseCase)).toList();

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    for (int i = 0; i < cards.size(); ++i) {
      if (i % GRID_WIDTH == 0) {
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(true);
        add(horizontalLayout);
      }
      horizontalLayout.add(cards.get(i));
    }

    add(new Button("New Actor", newActorListener()));
  }

  private ComponentEventListener<ClickEvent<Button>> newActorListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors"));
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
