package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.theme.lumo.LumoIcon;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author jorge
 */
public class InteractionLayout extends HorizontalLayout {

  public InteractionLayout(
      GetSessionDetailsUseCase.InteractionDto dto, OneUuidVoidOperator deleteListener) {
    super();
    setMargin(true);

    Avatar avatar = new Avatar(dto.actorName());
    avatar.setHeight("96px");
    avatar.setWidth("96px");

    if (dto.portrait().length > 0) {
      avatar.setImageHandler(
          DownloadHandler.fromInputStream(
              (DownloadEvent downloadEvent) -> {
                try (OutputStream outputStream = downloadEvent.getOutputStream()) {
                  outputStream.write(dto.portrait());
                }
                return new DownloadResponse(
                    new ByteArrayInputStream(dto.portrait()),
                    "avatar",
                    "image/png",
                    dto.portrait().length);
              }));
    } else {
      avatar.setColorIndex(Math.abs(dto.actorName().hashCode()) % 5);
    }

    VerticalLayout messageLayout = new VerticalLayout();
    messageLayout.setSpacing(false);
    messageLayout.setPadding(false);
    messageLayout
        .getElement()
        .appendChild(
            ElementFactory.createStrong(dto.actorName()),
            ElementFactory.createLabel(dto.timestamp().toString()));
    messageLayout.add(new Div(new Text(dto.spokenText())));

    HorizontalLayout operationsLayout = new HorizontalLayout();
    operationsLayout.setSpacing(false);
    operationsLayout.setPadding(false);

    Icon deleteIcon = LumoIcon.CROSS.create();
    deleteIcon.setColor("red");
    Button deleteButton = new Button(deleteIcon);
    deleteButton.addClickListener(e -> deleteListener.op(dto.id()));

    operationsLayout.add(deleteButton);

    add(avatar, messageLayout, operationsLayout);
  }

  /**
   * Helper interfacte to be able to send the delete listener to this component. I use this because
   * the delete use case can't be moved to this component because at the moment we don't refresh the
   * view to display the interactions. When we do it, we can move the use case here and remove this.
   */
  public interface OneUuidVoidOperator {
    public void op(UUID a);
  }
}
