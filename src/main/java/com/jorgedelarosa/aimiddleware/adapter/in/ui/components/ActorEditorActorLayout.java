package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author jorge
 */
public class ActorEditorActorLayout extends VerticalLayout {

  private final TextField name;
  private final TextArea physicalDescription;
  private final TextArea personality;
  private byte[] portraitBytes;

  public ActorEditorActorLayout(GetActorDetailsUseCase.ActorDto actorDto) {
    name = new TextField("Name");
    name.setValue(actorDto.name());
    name.setRequired(true);

    Image portrait;
    if (actorDto.portrait() != null && actorDto.portrait().length > 0) {
      portraitBytes = Arrays.copyOf(actorDto.portrait(), actorDto.portrait().length);
      portrait = imageFromBytes("Portrait", portraitBytes);
    } else {
      portrait = new Image();
    }

    physicalDescription = new TextArea("Physical description");
    physicalDescription.setValue(actorDto.physicalDescription());

    personality = new TextArea("Personality");
    actorDto.mind().ifPresent(e -> personality.setValue(e.personality()));

    Upload upload =
        new Upload(
            UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] data) -> {
                  portraitBytes = Arrays.copyOf(data, data.length);
                }));
    upload.setDropAllowed(true);
    upload.setMaxFiles(1);

    FormLayout formLayout = new FormLayout();
    formLayout.add(new Span(name, portrait), 2);
    formLayout.add(physicalDescription, 2);
    formLayout.add(personality, 2);
    formLayout.add(upload, 2);

    add(formLayout);
  }

  private Image imageFromBytes(String label, byte[] data) {
    return new Image(
        DownloadHandler.fromInputStream(
            (DownloadEvent downloadEvent) -> {
              try (OutputStream outputStream = downloadEvent.getOutputStream()) {
                outputStream.write(data);
              }
              return new DownloadResponse(
                  new ByteArrayInputStream(data), label, "image/png", data.length);
            }),
        label);
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
}