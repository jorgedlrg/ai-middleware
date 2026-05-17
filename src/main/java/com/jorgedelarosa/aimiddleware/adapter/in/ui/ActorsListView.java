package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorCard;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase.ActorDto;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Route(value = "actors-list", layout = MainView.class)
@PageTitle("Actors")
@RequiredArgsConstructor
public class ActorsListView extends Composite<VerticalLayout> implements BeforeEnterObserver {

  private final GetActorsUseCase getActorsUseCase;
  private final DeleteActorUseCase deleteActorUseCase;

  private TextField searchField;
  private ComboBox<String> sortCombo;
  private HorizontalLayout cardsLayout;

  private void buildFilterBar() {
    searchField = new TextField();
    searchField.setPlaceholder("Search by name...");
    searchField.setClearButtonVisible(true);
    searchField.addValueChangeListener(e -> applyFilterAndSort());

    sortCombo = new ComboBox<>();
    sortCombo.setItems("Name (A-Z)", "Name (Z-A)");
    sortCombo.setValue("Name (A-Z)");
    sortCombo.setClearButtonVisible(false);
    sortCombo.addValueChangeListener(e -> applyFilterAndSort());

    HorizontalLayout filterBar = new HorizontalLayout(searchField, sortCombo);
    filterBar.addClassName(LumoUtility.Gap.MEDIUM);
    getContent().add(filterBar);
  }

  private void applyFilterAndSort() {
    List<ActorDto> dtos = getActorsUseCase.execute();

    String searchText = searchField.getValue() != null ? searchField.getValue().toLowerCase() : "";
    List<ActorDto> filtered = dtos.stream()
        .filter(dto -> dto.name().toLowerCase().contains(searchText))
        .toList();

    String sortOrder = sortCombo.getValue();
    Comparator<ActorDto> comparator = switch (sortOrder) {
      case "Name (Z-A)" -> Comparator.<ActorDto>comparingDouble(d -> 0).reversed();
      default -> Comparator.comparing(ActorDto::name);
    };
    if ("Name (Z-A)".equals(sortOrder)) {
      comparator = Comparator.comparing(ActorDto::name).reversed();
    }

    List<ActorDto> sorted = filtered.stream().sorted(comparator).toList();

    cardsLayout.removeAll();
    sorted.stream().map(dto -> new ActorCard(dto, deleteActorUseCase)).forEach(cardsLayout::add);
  }

  private void render() {
    getContent().removeAll();

    cardsLayout = new HorizontalLayout();
    cardsLayout.addClassNames(LumoUtility.FlexWrap.WRAP);

    buildFilterBar();
    getContent().add(cardsLayout);

    applyFilterAndSort();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
