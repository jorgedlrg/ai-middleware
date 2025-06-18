package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.GetScenariosUseCase;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route(value = "scenarios", layout = MainView.class)
@PageTitle("Scenarios")
public class ScenariosView extends VerticalLayout {

  private final GetScenariosUseCase getScenariosUseCase;

  private final Grid<GetScenariosUseCase.ScenarioDto> scenariosGrid;

  public ScenariosView(GetScenariosUseCase getScenariosUseCase) {
    this.getScenariosUseCase = getScenariosUseCase;

    scenariosGrid = new Grid<>();
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::name).setHeader("Scenario");
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::contexts).setHeader("# contexts");
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::roles).setHeader("# roles");
    fillSessionsGrid();

    add(scenariosGrid);
  }

  private void fillSessionsGrid() {
    scenariosGrid.setItems(getScenariosUseCase.execute(new GetScenariosUseCase.Command()));
  }
}
