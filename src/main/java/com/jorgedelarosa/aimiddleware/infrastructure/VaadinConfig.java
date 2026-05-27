package com.jorgedelarosa.aimiddleware.infrastructure;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.lumo.Lumo;

@Push(PushMode.MANUAL)
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
public class VaadinConfig implements AppShellConfigurator {
  @Override
  public void configurePage(final AppShellSettings settings) {
    settings.addFavIcon("icon", "/favicon-32.svg", "32x32");
    settings.addFavIcon("icon", "/favicon-64.svg", "64x64");
    settings.addFavIcon("icon", "/favicon-128.svg", "128x128");
    settings.addFavIcon("icon", "/favicon-192.svg", "192x192");
  }
}