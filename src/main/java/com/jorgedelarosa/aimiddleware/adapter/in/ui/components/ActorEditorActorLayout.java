package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public class ActorEditorActorLayout extends VerticalLayout {

  private final TextField name;
  private final TextArea physicalDescription;
  private final TextArea personality;
  private byte[] portraitBytes;
  private final ComboBox<GetOutfitsUseCase.OutfitDto> outfitComboBox;

  public ActorEditorActorLayout(
      GetActorDetailsUseCase.ActorDto actorDto, List<GetOutfitsUseCase.OutfitDto> outfits) {
    name = new TextField("Name");
    name.setValue(actorDto.name());
    name.setRequired(true);

    Image portrait;
    if (actorDto.portrait() != null && actorDto.portrait().length > 0) {
      portraitBytes = Arrays.copyOf(actorDto.portrait(), actorDto.portrait().length);
      portrait = new ByteImage("Portrait", portraitBytes);
    } else {
      portraitBytes = new byte[0];
      portrait = new Image();
    }

    portrait.setMaxHeight("480px");

    physicalDescription = new TextArea("Physical description");
    physicalDescription.setValue(actorDto.physicalDescription());
    physicalDescription.setWidthFull();
    physicalDescription.setMinRows(4);

    personality = new TextArea("Personality");
    personality.setWidthFull();
    personality.setMinRows(4);
    actorDto.mind().ifPresent(e -> personality.setValue(e.personality()));

    Upload upload =
        new Upload(
            UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] data) -> {
                  portraitBytes = Arrays.copyOf(data, data.length);
                }));
    upload.setDropAllowed(true);
    upload.setMaxFiles(1);

    outfitComboBox = new ComboBox<>("Current outfit");
    outfitComboBox.setClearButtonVisible(true);
    outfitComboBox.setItems(outfits);
    outfitComboBox.setItemLabelGenerator(GetOutfitsUseCase.OutfitDto::name);
    if (actorDto.currentOutfit().isPresent()) {
      outfitComboBox.setValue(
          outfits.stream()
              .filter(e -> e.id().equals(actorDto.currentOutfit().get()))
              .findFirst()
              .orElseThrow());
    }

    FormLayout formLayout = new FormLayout();
    formLayout.setAutoResponsive(false);
    formLayout.addFormRow(portrait, name, outfitComboBox);
    formLayout.addFormRow(physicalDescription);
    formLayout.addFormRow(personality);
    formLayout.addFormRow(upload);
    formLayout.setWidthFull();

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

  public byte[] getPortraitBytes() {
    return portraitBytes;
  }

  public UUID getOutfitValue() {
    if (outfitComboBox.getValue() != null) {
      return outfitComboBox.getValue().id();
    } else {
      return null;
    }
  }
}