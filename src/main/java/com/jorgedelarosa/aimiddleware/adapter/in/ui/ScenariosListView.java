package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;

/**
 * @author jorge
 */
@Route(value = "scenarios-list", layout = MainView.class)
@PageTitle("Scenarios")
public class ScenariosListView extends VerticalLayout {

  private final GetScenariosUseCase getScenariosUseCase;

  private Grid<GetScenariosUseCase.ScenarioDto> scenariosGrid;

  private List<GetScenariosUseCase.ScenarioDto> allScenarios;

  public ScenariosListView(GetScenariosUseCase getScenariosUseCase) {
    this.getScenariosUseCase = getScenariosUseCase;

    setSizeFull();
    setPadding(true);
    setSpacing(true);

    refreshData();
    add(createGrid());
  }

  private Grid<GetScenariosUseCase.ScenarioDto> createGrid() {
    scenariosGrid = new Grid<>(GetScenariosUseCase.ScenarioDto.class, false);
    scenariosGrid.setWidthFull();
    scenariosGrid.setHeightFull();

    scenariosGrid
        .addColumn(GetScenariosUseCase.ScenarioDto::name)
        .setHeader("Scenario")
        .setSortable(true)
        .setFlexGrow(1);
    scenariosGrid
        .addColumn(dto -> String.join(", ", dto.contextNames()))
        .setHeader("Contexts")
        .setSortable(true)
        .setFlexGrow(3);
    scenariosGrid
        .addColumn(dto -> String.join(", ", dto.roleNames()))
        .setHeader("Roles")
        .setSortable(true)
        .setFlexGrow(3);
    scenariosGrid
        .addColumn(GetScenariosUseCase.ScenarioDto::introductionCount)
        .setHeader("Intro")
        .setSortable(true)
        .setFlexGrow(0);

    scenariosGrid.addItemClickListener(editScenarioListener());

    scenariosGrid.setItems(allScenarios);

    return scenariosGrid;
  }

  private void refreshData() {
    allScenarios = getScenariosUseCase.execute(new GetScenariosUseCase.Command());
  }

  private ComponentEventListener<ItemClickEvent<GetScenariosUseCase.ScenarioDto>>
      editScenarioListener() {
    return (ItemClickEvent<GetScenariosUseCase.ScenarioDto> t) -> {
      t.getColumn().getUI().ifPresent(ui -> ui.navigate("scenarios/" + t.getItem().id()));
    };
  }
}
