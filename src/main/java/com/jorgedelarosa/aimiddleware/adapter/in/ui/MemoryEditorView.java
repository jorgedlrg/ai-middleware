package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "/actors/:actorId?/memory", layout = MainView.class)
@RequiredArgsConstructor
public class MemoryEditorView extends VerticalLayout
    implements BeforeEnterObserver, HasDynamicTitle {

  private String pageTitle;

  private void render() {}

  @Override
  public void beforeEnter(BeforeEnterEvent bee) {

    render();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
