package com.jorgedelarosa.aimiddleware.infrastructure;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;

/**
 * @author jorge
 */
@Push(PushMode.MANUAL)
public class VaadinConfig implements AppShellConfigurator {}
