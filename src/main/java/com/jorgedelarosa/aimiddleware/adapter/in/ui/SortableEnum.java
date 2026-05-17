package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import java.util.Comparator;

public interface SortableEnum<T> {

  String displayName();

  Comparator<T> comparator();

  static <T> Comparator<T> nullsLast(Comparator<T> c) {
    return Comparator.nullsLast(c);
  }

  static <T> Comparator<T> nullsFirst(Comparator<T> c) {
    return Comparator.nullsFirst(c);
  }
}