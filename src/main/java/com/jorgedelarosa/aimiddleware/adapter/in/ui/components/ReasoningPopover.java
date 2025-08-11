package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * @author jorge
 */
public class ReasoningPopover extends Popover {

  public ReasoningPopover(Component content, Component target) {
    super(content);
    content.addClassNames(LumoUtility.Padding.MEDIUM);
    setTarget(target);
    addThemeVariants(PopoverVariant.ARROW, PopoverVariant.LUMO_NO_PADDING);
    setPosition(PopoverPosition.BOTTOM);
    setModal(true);

    Tooltip tooltip =
        Tooltip.forComponent(target)
            .withText("Click to view reasoning")
            .withPosition(Tooltip.TooltipPosition.TOP_START)
            .withHoverDelay(0);
  }
}
