package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.user.GetUserSettingsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.user.UpdateUserSettingsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "user-settings", layout = MainView.class)
@PageTitle("User settings")
@RequiredArgsConstructor
public class UserSettingsView extends VerticalLayout implements BeforeEnterObserver {

  private final GetUserSettingsUseCase getUserSettingsUseCase;
  private final UpdateUserSettingsUseCase updateUserSettingsUseCase;

  private final UUID user = UUID.fromString("857fa610-b987-454c-96c3-bbf5354f13a0"); // FIXME

  private final CheckboxGroup<String> textGenFeatures = new CheckboxGroup<>();
  private final RadioButtonGroup<String> textGenProvider = new RadioButtonGroup<>();
  private final TextField openrouterApikey = new TextField("Api key");
  private final TextField openrouterModel = new TextField("Model");
  private final TextField ollamaHost = new TextField("Host URL");
  private final TextField ollamaModel = new TextField("Model");

  private void render() {
    removeAll();

    GetUserSettingsUseCase.SettingsDto dto =
        getUserSettingsUseCase.execute(new GetUserSettingsUseCase.Command(user));

    // Features
    textGenFeatures.setLabel("Text Generation features:");
    textGenFeatures.setItems("Actions", "Mood", "Thoughts");
    if (dto.actionsEnabled()) {
      textGenFeatures.select("Actions");
    }
    if (dto.moodEnabled()) {
      textGenFeatures.select("Mood");
    }
    if (dto.thoughtsEnabled()) {
      textGenFeatures.select("Thoughts");
    }
    add(textGenFeatures);

    // Text Gen provider
    textGenProvider.setLabel("Text generation provider");
    textGenProvider.setItems("Openrouter", "Ollama");
    if (dto.textgenProvider().equals("openrouter")) {
      textGenProvider.setValue("Openrouter");
    } else {
      textGenProvider.setValue("Ollama");
    }
    add(textGenProvider);

    // Text Gen providers settings
    Accordion accordion = new Accordion();
    FormLayout openrouterSettingsForm = new FormLayout();
    if (dto.openrouterApikey() != null) {
      openrouterApikey.setValue(dto.openrouterApikey());
    }
    openrouterSettingsForm.add(openrouterApikey, 1);
    if (dto.openrouterModel() != null) {
      openrouterModel.setValue(dto.openrouterModel());
    }
    openrouterSettingsForm.add(openrouterModel, 1);
    accordion.add("Openrouter", openrouterSettingsForm);

    FormLayout ollamaSettingsForm = new FormLayout();
    if (dto.ollamaHost() != null) {
      ollamaHost.setValue(dto.ollamaHost());
    }
    ollamaSettingsForm.add(ollamaHost, 1);
    if (dto.ollamaModel() != null) {
      ollamaModel.setValue(dto.ollamaModel());
    }
    ollamaSettingsForm.add(ollamaModel, 1);
    accordion.add("Ollama", ollamaSettingsForm);
    if (dto.textgenProvider().equals("openrouter")) {
      accordion.open(0);
    } else {
      accordion.open(1);
    }
    add(accordion);

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveSettingsListener());
    add(saveButton);
  }

  private ComponentEventListener<ClickEvent<Button>> saveSettingsListener() {
    return (ClickEvent<Button> t) -> {
      updateUserSettingsUseCase.execute(
          new UpdateUserSettingsUseCase.Command(
              user,
              textGenProvider.getValue().toLowerCase(),
              openrouterApikey.getValue(),
              openrouterModel.getValue(),
              ollamaHost.getValue(),
              ollamaModel.getValue(),
              textGenFeatures.getSelectedItems().contains("Actions"),
              textGenFeatures.getSelectedItems().contains("Mood"),
              textGenFeatures.getSelectedItems().contains("Thoughts")));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("user-settings"));
      Notification notification = Notification.show("Settings saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
