package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.GetActorDetailsUseCase;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author jorge
 */
public class ActorEditorActorLayout extends VerticalLayout {

  private final TextField name;
  private final TextArea physicalDescription;

  public ActorEditorActorLayout(GetActorDetailsUseCase.ActorDto actorDto) {
    name = new TextField("Name");
    name.setValue(actorDto.name());
    name.setRequired(true);

    physicalDescription = new TextArea("Physical description");
    physicalDescription.setValue(actorDto.physicalDescription());

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 0);
    formLayout.add(physicalDescription, 0);

    add(formLayout);
  }

  public String getNameValue() {
    return name.getValue();
  }

  public String getPhysicalDescriptionValue() {
    return physicalDescription.getValue();
  }
}