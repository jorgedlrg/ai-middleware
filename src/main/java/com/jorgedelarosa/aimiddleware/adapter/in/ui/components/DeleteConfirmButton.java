package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;

/**
 * @author jorge
 */
public class DeleteConfirmButton extends Button {

  public DeleteConfirmButton(
      String label, String itemLabel, ComponentEventListener<ConfirmEvent> deleteListener) {
    super(label);

    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setHeader("Delete \"" + itemLabel + "\"?");
    dialog.setText("Are you sure you want to permanently delete this?");

    dialog.setCancelable(true);

    dialog.setConfirmText("Delete");
    dialog.setConfirmButtonTheme("error primary");
    dialog.addConfirmListener(deleteListener);

    addClickListener(event -> dialog.open());
    addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
  }
}
