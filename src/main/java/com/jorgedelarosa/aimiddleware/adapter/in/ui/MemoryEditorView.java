package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.MemoryFragmentLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "/actors/:actorId?/memory", layout = MainView.class)
@RequiredArgsConstructor
public class MemoryEditorView extends VerticalLayout
    implements BeforeEnterObserver, HasDynamicTitle {

  private final GetMemoryUseCase getMemoryUseCase;

  private UUID actor;
  private GetMemoryUseCase.MemoryDto dto;

  private VirtualList<GetMemoryUseCase.MemoryFragmentDto> fragmentList;

  private String pageTitle;

  private void render() {
    fragmentList = new VirtualList<>();
    fragmentList.setItems(dto.fragments());
    fragmentList.setRenderer(memoryFragmentRenderer);

    add(fragmentList);
  }

  private final ComponentRenderer<Component, GetMemoryUseCase.MemoryFragmentDto>
      memoryFragmentRenderer =
          new ComponentRenderer<>(
              fragment -> {
                MemoryFragmentLayout layout = new MemoryFragmentLayout(fragment);
                return layout;
              });

  @Override
  public void beforeEnter(BeforeEnterEvent bee) {
    actor = UUID.fromString(bee.getRouteParameters().get("actorId").orElseThrow());
    if (actor != null) {
      dto = getMemoryUseCase.execute(new GetMemoryUseCase.Command(actor));
      pageTitle = "Memory Editor - " + dto.actorId();
    } else {
      pageTitle = "Memory Editor - new";
    }

    render();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
