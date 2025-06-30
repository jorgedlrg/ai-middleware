package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "new-session", layout = MainView.class)
@RequiredArgsConstructor
@PageTitle("New Session")
public class NewSessionView extends VerticalLayout implements BeforeEnterObserver {
  private final GetScenariosUseCase getScenariosUseCase;

  private void render() {
    removeAll();
    ComboBox<GetScenariosUseCase.ScenarioDto> scenariosComboBox = new ComboBox<>("Scenarios");
    scenariosComboBox.setItems(getScenariosUseCase.execute(new GetScenariosUseCase.Command()));
    scenariosComboBox.setItemLabelGenerator(GetScenariosUseCase.ScenarioDto::name);
    scenariosComboBox.addValueChangeListener(e -> selectScenarioListener(e.getValue()));

    add(scenariosComboBox);
  }

  private void selectScenarioListener(GetScenariosUseCase.ScenarioDto dto) {
    // TODO

    render();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
