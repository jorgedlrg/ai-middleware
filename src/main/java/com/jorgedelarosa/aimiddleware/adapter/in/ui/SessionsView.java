package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.GetSessionsUseCase;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route(value = "sessions", layout = MainView.class)
@PageTitle("Sessions")
public class SessionsView extends VerticalLayout {
  private final GetSessionsUseCase getSessionsUseCase;

  private final Grid<GetSessionsUseCase.SessionDto> sessionsGrid;

  public SessionsView(GetSessionsUseCase getSessionsUseCase) {
    this.getSessionsUseCase = getSessionsUseCase;

    sessionsGrid = new Grid<>();
    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::session).setHeader("Session");
    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::scenario).setHeader("Scenario");
    sessionsGrid.setItems(this.getSessionsUseCase.execute(new GetSessionsUseCase.Command()));
    
    add(sessionsGrid);
  }
}
