package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route(value = "sessions-list", layout = MainView.class)
@PageTitle("Sessions")
public class SessionsListView extends VerticalLayout {

  // TODO: The idea here will be to be able to select a session and modify it, and create a new
  // session maybe? or do it from Scenarios
  private final GetSessionsUseCase getSessionsUseCase;

  private final Grid<GetSessionsUseCase.SessionDto> sessionsGrid;

  public SessionsListView(GetSessionsUseCase getSessionsUseCase) {
    this.getSessionsUseCase = getSessionsUseCase;

    sessionsGrid = new Grid<>();
    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::session).setHeader("Session");
    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::scenario).setHeader("Scenario");
    sessionsGrid.addItemClickListener(sessionListener());
    fillSessionsGrid();

    Button newSession = new Button("Create new Session");
    newSession.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    newSession.addClickListener(createNewSession());

    add(sessionsGrid);
    add(newSession);
  }

  private void fillSessionsGrid() {
    sessionsGrid.setItems(this.getSessionsUseCase.execute(new GetSessionsUseCase.Command()));
  }

  /**
   * Navigate to the clicked session
   *
   * @return
   */
  private ComponentEventListener<ItemClickEvent<GetSessionsUseCase.SessionDto>> sessionListener() {
    return (ItemClickEvent<GetSessionsUseCase.SessionDto> t) -> {
      t.getColumn().getUI().ifPresent(ui -> ui.navigate("sessions/" + t.getItem().session()));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> createNewSession() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("new-session"));
    };
  }
}
