package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase.ActorDto;
import java.util.Comparator;

public enum ActorSortOption implements SortableEnum<ActorDto> {
  NAME_ASC("Name (A-Z)", Comparator.comparing(ActorDto::name)),
  NAME_DESC("Name (Z-A)", Comparator.comparing(ActorDto::name).reversed()),
  MODIFIED_DESC(
      "Last Modified (newest)",
      SortableEnum.nullsLast(Comparator.comparing(ActorDto::updatedAt).reversed())),
  MODIFIED_ASC(
      "Last Modified (oldest)", SortableEnum.nullsLast(Comparator.comparing(ActorDto::updatedAt)));

  private final String displayName;
  private final Comparator<ActorDto> comparator;

  ActorSortOption(String displayName, Comparator<ActorDto> comparator) {
    this.displayName = displayName;
    this.comparator = comparator;
  }

  @Override
  public String displayName() {
    return displayName;
  }

  @Override
  public Comparator<ActorDto> comparator() {
    return comparator;
  }
}