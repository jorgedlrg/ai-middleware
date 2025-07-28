package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route(value = "scenarios-list", layout = MainView.class)
@PageTitle("Scenarios")
public class ScenariosListView extends VerticalLayout {

  private final GetScenariosUseCase getScenariosUseCase;

  private final Grid<GetScenariosUseCase.ScenarioDto> scenariosGrid;

  public ScenariosListView(GetScenariosUseCase getScenariosUseCase) {
    this.getScenariosUseCase = getScenariosUseCase;

    scenariosGrid = new Grid<>();
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::name).setHeader("Scenario");
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::contexts).setHeader("# contexts");
    scenariosGrid.addColumn(GetScenariosUseCase.ScenarioDto::roles).setHeader("# roles");
    scenariosGrid
        .addColumn(GetScenariosUseCase.ScenarioDto::introductions)
        .setHeader("# introductions");
    scenariosGrid.addItemClickListener(editScenarioListener());
    fillSessionsGrid();

    add(scenariosGrid);
  }

  private void fillSessionsGrid() {
    scenariosGrid.setItems(getScenariosUseCase.execute(new GetScenariosUseCase.Command()));
  }

  private ComponentEventListener<ItemClickEvent<GetScenariosUseCase.ScenarioDto>>
      editScenarioListener() {
    return (ItemClickEvent<GetScenariosUseCase.ScenarioDto> t) -> {
      t.getColumn().getUI().ifPresent(ui -> ui.navigate("scenarios/" + t.getItem().id()));
    };
  }
}
