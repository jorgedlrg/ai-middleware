package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteOutfitUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveOutfitUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "/outfits/:outfitId?", layout = MainView.class)
@RequiredArgsConstructor
public class OutfitEditorView extends VerticalLayout
    implements HasDynamicTitle, BeforeEnterObserver {

  private final GetOutfitDetailsUseCase getOutfitDetailsUseCase;
  private final SaveOutfitUseCase saveOutfitUseCase;
  private final DeleteOutfitUseCase deleteOutfitUseCase;

  private String pageTitle;
  private GetOutfitDetailsUseCase.OutfitDto dto;

  private UUID id;
  private TextField name;
  private TextArea description;

  private void render() {
    removeAll();

    name = new TextField("Name");
    description = new TextArea("Description");
    if (dto != null) {
      name.setValue(dto.name());
      description.setValue(dto.description());
    }

    FormLayout formLayout = new FormLayout();
    formLayout.add(name, 1);
    formLayout.add(description, 2);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveOutfitListener());

    add(formLayout);
    add(
        new Div(
            saveButton,
            new DeleteConfirmButton("Delete", name.getValue(), deleteOutfitListener())));
  }

  private ComponentEventListener<ClickEvent<Button>> saveOutfitListener() {
    return (ClickEvent<Button> t) -> {
      saveOutfitUseCase.execute(
          new SaveOutfitUseCase.Command(id, name.getValue(), description.getValue()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("outfits-list"));
      Notification notification = Notification.show(name.getValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteOutfitListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteOutfitUseCase.execute(new DeleteOutfitUseCase.Command(id));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("outfits-list"));
      Notification notification = Notification.show(dto.name() + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<String> opt = event.getRouteParameters().get("outfitId");
    if (opt.isPresent()) {
      dto =
          getOutfitDetailsUseCase.execute(
              new GetOutfitDetailsUseCase.Command(UUID.fromString(opt.get())));
      id = dto.id();
      pageTitle = "Outfit Editor - " + dto.name();
    } else {
      pageTitle = "Outfit Editor - new";
    }

    render();
  }
}
