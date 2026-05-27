package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GenerateActorPortraitUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
  private final TextArea profile;
  private final TextArea physicalDescription;
  private final TextArea personality;
  private byte[] portraitBytes;
  private final ComboBox<GetOutfitsUseCase.OutfitDto> outfitComboBox;
  private final Image portrait;
  private final UUID actorId;

  public ActorEditorActorLayout(
      GetActorDetailsUseCase.ActorDto actorDto,
      List<GetOutfitsUseCase.OutfitDto> outfits,
      GenerateActorPortraitUseCase generateActorPortraitUseCase) {
    this.actorId = actorDto.id();
    name = new TextField("Name");
    name.setValue(actorDto.name());
    name.setRequired(true);

    portrait = new Image(buildPortraitUrl(), "Portrait");
    portrait.setMaxHeight("480px");

    portraitBytes = new byte[0];

    profile = new TextArea("Profile");
    profile.setValue(actorDto.profile());
    profile.setWidthFull();
    profile.setMinRows(4);

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

    var portraitActions = new HorizontalLayout();
    if (actorDto.id() != null) {
      Button generateButton = new Button("Generate with ComfyUI");
      generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      generateButton.addClickListener(e -> {
        generateButton.setEnabled(false);
        generateButton.setText("Generating...");
        try {
          String promptId =
              generateActorPortraitUseCase.execute(
                  new GenerateActorPortraitUseCase.Command(actorDto.id()));
          Notification notification = Notification.show(
              "Portrait generation requested (promptId: " + promptId + ")");
          notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } finally {
          generateButton.setEnabled(true);
          generateButton.setText("Generate with ComfyUI");
        }
      });
      portraitActions.add(generateButton);

      Button refreshButton = new Button("Refresh portrait");
      refreshButton.addClickListener(e -> {
        portrait.setSrc(buildPortraitUrl());
        Notification.show("Portrait refreshed");
      });
      portraitActions.add(refreshButton);
    } else {
      portraitActions.add(new Span("Save the actor first to enable portrait generation"));
    }

    FormLayout formLayout = new FormLayout();
    formLayout.setAutoResponsive(false);
    formLayout.addFormRow(portrait, name, outfitComboBox);
    formLayout.addFormRow(profile);
    formLayout.addFormRow(physicalDescription);
    formLayout.addFormRow(personality);
    formLayout.addFormRow(upload);
    formLayout.addFormRow(portraitActions);
    formLayout.setWidthFull();

    add(formLayout);
  }

  private String buildPortraitUrl() {
    if (actorId == null) return "";
    return "/api/v1/actor/actors/" + actorId + "/portrait?t=" + System.currentTimeMillis();
  }

  public String getNameValue() {
    return name.getValue();
  }

  public String getProfileValue() {
    return profile.getValue();
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