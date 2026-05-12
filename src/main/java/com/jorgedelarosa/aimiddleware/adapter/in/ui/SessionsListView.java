package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionsUseCase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jorge
 */
@Route(value = "sessions-list", layout = MainView.class)
@PageTitle("Sessions")
public class SessionsListView extends VerticalLayout {

  private final GetSessionsUseCase getSessionsUseCase;
  private final GetScenariosUseCase getScenariosUseCase;

  private Grid<GetSessionsUseCase.SessionDto> sessionsGrid;
  private ComboBox<String> scenarioFilter;

  private List<GetSessionsUseCase.SessionDto> allSessions;

  public SessionsListView(GetSessionsUseCase getSessionsUseCase, GetScenariosUseCase getScenariosUseCase) {
    this.getSessionsUseCase = getSessionsUseCase;
    this.getScenariosUseCase = getScenariosUseCase;

    setSizeFull();
    setPadding(true);
    setSpacing(true);

    refreshData();
    HorizontalLayout filterBar = createFilterBar();
    Grid<GetSessionsUseCase.SessionDto> grid = createGrid();
    add(filterBar, grid);
  }

  private HorizontalLayout createFilterBar() {
    HorizontalLayout filterBar = new HorizontalLayout();
    filterBar.setWidthFull();
    filterBar.setSpacing(true);
    filterBar.setAlignItems(FlexComponent.Alignment.BASELINE);

    scenarioFilter = new ComboBox<>("Filter by Scenario");
    scenarioFilter.setWidth("300px");
    scenarioFilter.setClearButtonVisible(true);
    scenarioFilter.addValueChangeListener(e -> applyFilter());

    List<String> scenarioNames =
        getScenariosUseCase.execute(new GetScenariosUseCase.Command()).stream()
            .map(GetScenariosUseCase.ScenarioDto::name)
            .sorted()
            .collect(Collectors.toList());
    scenarioFilter.setItems(scenarioNames);

    filterBar.add(scenarioFilter);
    return filterBar;
  }

  private Grid<GetSessionsUseCase.SessionDto> createGrid() {
    sessionsGrid = new Grid<>(GetSessionsUseCase.SessionDto.class, false);
    sessionsGrid.setWidthFull();
    sessionsGrid.setHeightFull();

    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::scenario).setHeader("Scenario").setSortable(true).setFlexGrow(2);
    sessionsGrid.addColumn(dto -> String.join(", ", dto.participantNames())).setHeader("Participants").setSortable(true).setFlexGrow(4);
    sessionsGrid.addColumn(dto -> formatLastActivity(dto.lastActivity())).setHeader("Last Activity").setSortable(true).setFlexGrow(1);
    sessionsGrid.addColumn(GetSessionsUseCase.SessionDto::interactionCount).setHeader("Messages").setSortable(true).setFlexGrow(1);

    sessionsGrid.addItemClickListener(sessionListener());

    sessionsGrid.setItems(allSessions);

    return sessionsGrid;
  }

  private void refreshData() {
    allSessions = getSessionsUseCase.execute(new GetSessionsUseCase.Command());
  }

  private void applyFilter() {
    String selectedScenario = scenarioFilter.getValue();
    if (selectedScenario != null && !selectedScenario.isEmpty()) {
      List<GetSessionsUseCase.SessionDto> filtered =
          allSessions.stream()
              .filter(dto -> dto.scenario().equals(selectedScenario))
              .collect(Collectors.toList());
      sessionsGrid.setItems(filtered);
    } else {
      sessionsGrid.setItems(allSessions);
    }
  }

  private String formatLastActivity(java.time.Instant instant) {
    if (instant == null) {
      return "—";
    }
    var zonedDateTime = instant.atZone(ZoneId.systemDefault());
    var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
    return zonedDateTime.format(formatter);
  }

  private ComponentEventListener<ItemClickEvent<GetSessionsUseCase.SessionDto>> sessionListener() {
    return (ItemClickEvent<GetSessionsUseCase.SessionDto> t) -> {
      t.getColumn()
          .getUI()
          .ifPresent(ui -> ui.navigate("sessions/" + t.getItem().session() + "/interact"));
    };
  }
}
