package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
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
  private final TextArea personality;

  public ActorEditorActorLayout(GetActorDetailsUseCase.ActorDto actorDto) {
    name = new TextField("Name");
    name.setValue(actorDto.name());
    name.setRequired(true);

    physicalDescription = new TextArea("Physical description");
    physicalDescription.setValue(actorDto.physicalDescription());

    personality = new TextArea("Personality");
    actorDto.mind().ifPresent(e -> personality.setValue(e.personality()));

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 1);
    formLayout.add(physicalDescription, 2);
    formLayout.add(personality, 2);

    add(formLayout);
  }

  public String getNameValue() {
    return name.getValue();
  }

  public String getPhysicalDescriptionValue() {
    return physicalDescription.getValue();
  }

  public String getPersonalityValue() {
    return personality.getValue();
  }
}