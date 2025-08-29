package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * @author jorge
 */
public class MemoryFragmentLayout extends HorizontalLayout {

  public MemoryFragmentLayout(GetMemoryUseCase.MemoryFragmentDto dto) {
    super();
    add(new Text(dto.text()));
  }

}
