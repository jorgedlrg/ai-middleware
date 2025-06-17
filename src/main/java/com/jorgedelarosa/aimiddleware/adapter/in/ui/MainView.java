package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * @author jorge
 */
@Route("")
public class MainView extends VerticalLayout {

  public MainView() {
    Button interactButton = new Button("Interact", VaadinIcon.CHAT.create());
    interactButton.addClickListener(
        (e) -> {
          e.getSource().getUI().ifPresent(ui -> ui.navigate("interaction"));
        });

    add("AI Roleplay Middleware");
    add(interactButton);
  }
}
