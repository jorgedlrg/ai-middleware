package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author jorge
 */
public class InteractionLayout extends HorizontalLayout {
  private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public InteractionLayout(
      GetSessionDetailsUseCase.InteractionDto dto,
      OneUuidVoidOperator prevListener,
      OneUuidVoidOperator nextListener,
      OneUuidVoidOperator deleteListener) {
    super();
    setMargin(true);
    setPadding(false);

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
    messageLayout.setWidthFull();
    messageLayout.setSpacing(false);
    messageLayout.setPadding(false);

    Icon prevIcon = LumoIcon.ARROW_LEFT.create();
    Button prevButton = new Button(prevIcon);
    prevButton.addClickListener(e -> prevListener.op(dto.id()));

    Icon nextIcon = LumoIcon.ARROW_RIGHT.create();
    Button nextButton = new Button(nextIcon);
    nextButton.addClickListener(e -> nextListener.op(dto.id()));

    Icon deleteIcon = LumoIcon.CROSS.create();
    deleteIcon.setColor("red");
    Button deleteButton = new Button(deleteIcon);
    deleteButton.addClickListener(e -> deleteListener.op(dto.id()));

    HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setSpacing(false);
    buttonsLayout.addClassNames(LumoUtility.Border.NONE, LumoUtility.AlignItems.CENTER);
    buttonsLayout.add(
        new Div(new Text(dto.siblingNumber() + "/" + dto.totalSiblings())),
        prevButton,
        nextButton,
        deleteButton);

    // TODO: Get zone from user browser (requires current view)
    ZonedDateTime zdt = ZonedDateTime.ofInstant(dto.timestamp(), ZoneId.systemDefault());

    Div name = new Div(new Text(dto.actorName() + " " + dto.emoji()));
    name.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);
    HorizontalLayout nameLayout =
        new HorizontalLayout(
            new Div(
                name,
                new Text(
                    zdt.toLocalDateTime().format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)))),
            buttonsLayout);
    nameLayout.addClassNames(LumoUtility.Width.FULL, LumoUtility.JustifyContent.BETWEEN);
    messageLayout.add(nameLayout);

    String mood = "";
    if (dto.mood() != null && !dto.mood().equals("")) {
      mood = "(" + dto.mood() + ")";
    }
    Div thoughtText = new Div(new Text(dto.thoughtText()));
    thoughtText.setWidth("800px");
    Details thoughts = new Details("Thoughts " + mood + dto.emoji(), thoughtText);
    thoughts.setOpened(true);
    thoughts.addThemeVariants(DetailsVariant.SMALL);
    messageLayout.add(thoughts);
    if (dto.actionText() != null && !dto.actionText().equals("")) {
      Div action = new Div(new Text(dto.actionText()));
      action.addClassNames(
          LumoUtility.Background.CONTRAST_5, LumoUtility.TextColor.PRIMARY, LumoUtility.Width.AUTO);
      messageLayout.add(action);
    }
    Div text = new Div(new Text(dto.spokenText()));
    text.addClassNames(LumoUtility.Width.FULL);
    messageLayout.add(text);

    add(avatar, messageLayout);
    addClassNames(
        LumoUtility.Border.ALL,
        LumoUtility.BorderColor.CONTRAST,
        LumoUtility.BorderRadius.LARGE,
        LumoUtility.Margin.XSMALL,
        LumoUtility.Background.BASE);
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
