package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * @author jorge
 */
public class PerformanceCard extends Card implements ClickNotifier<Component> {

  public PerformanceCard(
      GetSessionDetailsUseCase.PerformanceDto dto,
      InteractionLayout.OneUuidVoidOperator machineInteractionListener) {
    super();
    Component portrait;
    if (dto.portrait().length > 0) {
      portrait = new ByteImage("Portrait", dto.portrait());
      ((ByteImage) portrait).setWidth("330px");
    } else {
      portrait = LumoIcon.PHOTO.create();
      portrait
          .getStyle()
          .setColor("var(--lumo-primary-color)")
          .setBackgroundColor("var(--lumo-primary-color-10pct)");
      ((Icon) portrait).setSize("330px");
    }
    setMedia(portrait);

    Div header = new Div();
    header.addClassNames(
        LumoUtility.Display.FLEX,
        LumoUtility.FlexDirection.COLUMN_REVERSE,
        LumoUtility.LineHeight.XSMALL);
    Div subtitle = new Div(dto.roleName());
    subtitle.addClassNames(
        LumoUtility.TextTransform.UPPERCASE,
        LumoUtility.FontSize.XSMALL,
        LumoUtility.TextColor.SECONDARY);
    header.add(new H2(dto.actorName()), subtitle);
    setHeader(header);

    Tooltip tooltip =
        Tooltip.forComponent(this)
            .withText("Click to generate interaction!")
            .withPosition(Tooltip.TooltipPosition.END);
    
    addClickListener(e -> machineInteractionListener.op(dto.role()));
    addThemeVariants(CardVariant.LUMO_ELEVATED);
  }
}
