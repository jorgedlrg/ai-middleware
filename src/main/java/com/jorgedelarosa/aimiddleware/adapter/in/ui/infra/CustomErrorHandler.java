package com.jorgedelarosa.aimiddleware.adapter.in.ui.infra;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jorge
 */
@Slf4j
public class CustomErrorHandler implements ErrorHandler {

  @Override
  public void error(ErrorEvent errorEvent) {
    log.error("Handling exception:", errorEvent.getThrowable());
    if (UI.getCurrent() != null) {
      UI.getCurrent()
          .access(
              () -> {
                Notification notification =
                    Notification.show(
                        "An internal error has occurred:\n"
                            + errorEvent.getThrowable().getLocalizedMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
              });
    }
  }
}