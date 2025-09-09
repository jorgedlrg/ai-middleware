package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "outfits-list", layout = MainView.class)
@PageTitle("Outfits")
@RequiredArgsConstructor
public class OutfitListView extends VerticalLayout implements BeforeEnterObserver {

  private final GetOutfitsUseCase getOutfitsUseCase;

  private void render() {
    removeAll();

    Grid<GetOutfitsUseCase.OutfitDto> grid;
    grid = new Grid<>();
    grid.addColumn(GetOutfitsUseCase.OutfitDto::name).setHeader("Outfit");
    grid.addItemClickListener(editOutfitListener());
    grid.setItems(getOutfitsUseCase.execute());
    add(grid);
  }

  private ComponentEventListener<ItemClickEvent<GetOutfitsUseCase.OutfitDto>> editOutfitListener() {
    return (ItemClickEvent<GetOutfitsUseCase.OutfitDto> t) -> {
      t.getColumn().getUI().ifPresent(ui -> ui.navigate("outfits/" + t.getItem().id()));
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
