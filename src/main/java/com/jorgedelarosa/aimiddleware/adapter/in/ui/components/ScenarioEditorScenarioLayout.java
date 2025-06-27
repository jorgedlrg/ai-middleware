package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.GetScenarioDetailsUseCase;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author jorge
 */
public class ScenarioEditorScenarioLayout extends VerticalLayout {

  private final TextField name;


  public ScenarioEditorScenarioLayout(GetScenarioDetailsUseCase.ScenarioDto scenarioDto) {
    name = new TextField("Name");
    name.setValue(scenarioDto.name());
    name.setRequired(true);

   

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 1);

    add(formLayout);
  }

  public String getNameValue() {
    return name.getValue();
  }

  
}