package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.MemoryFragmentLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteMemoryFragmentUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveMemoryUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
  private final SaveMemoryUseCase saveMemoryUseCase;
  private final DeleteMemoryFragmentUseCase deleteMemoryFragmentUseCase;

  private UUID actor;
  private GetMemoryUseCase.MemoryDto dto;

  // private VirtualList<GetMemoryUseCase.MemoryFragmentDto> fragmentList;
  private final List<MemoryFragmentLayout> fragments = new ArrayList<>();

  private String pageTitle;

  private void render() {
    removeAll();

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveMemoryListener());
    Button addFragmentButton = new Button("Add Memory Fragment");
    addFragmentButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    addFragmentButton.addClickListener(addFragmentListener());

    fragments.stream().forEach(e -> add(e));
    add(new Div(saveButton, addFragmentButton));
  }

  private ComponentEventListener<ClickEvent<Button>> addFragmentListener() {
    return (ClickEvent<Button> t) -> {
      fragments.add(
          new MemoryFragmentLayout(
              deleteMemoryFragmentUseCase,
              actor,
              new GetMemoryUseCase.MemoryFragmentDto(null, null, Instant.now(), true)));
      render();
    };
  }

  private ComponentEventListener<ClickEvent<Button>> saveMemoryListener() {
    return (ClickEvent<Button> t) -> {
      saveMemoryUseCase.execute(
          new SaveMemoryUseCase.Command(
              actor,
              fragments.stream()
                  .map(
                      e ->
                          new SaveMemoryUseCase.MemoryFragmentDto(
                              e.getFragmentId(), e.getText(), e.getTimestamp(), e.getEnabled()))
                  .toList()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification = Notification.show("Memory saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent bee) {
    actor = UUID.fromString(bee.getRouteParameters().get("actorId").orElseThrow());
    if (actor != null) {
      dto = getMemoryUseCase.execute(new GetMemoryUseCase.Command(actor));
      fragments.clear();
      dto.fragments().stream()
          .forEach(
              e -> fragments.add(new MemoryFragmentLayout(deleteMemoryFragmentUseCase, actor, e)));
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
