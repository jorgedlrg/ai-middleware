package com.jorgedelarosa.aimiddleware.adapter.in.ui.infra;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@Slf4j
public class ServiceListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent event) {

    event
        .getSource()
        .addSessionInitListener(
            initEvent -> {
              VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());
              log.info("A new Session has been initialized!");
            });

    event.getSource().addUIInitListener(initEvent -> log.info("A new UI has been initialized!"));
  }
}
