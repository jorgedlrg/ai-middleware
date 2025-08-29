package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * I'm using now the actor ID to identify this aggregate, although the structure doesn't reflect
 * this. I'm still unsure about the design of this aggregate.
 *
 * @author jorge
 */
public class Memory extends AggregateRoot {

  private final UUID actor;
  private final List<MemoryFragment> fragments;

  private Memory(UUID actor, List<MemoryFragment> fragments, Class clazz, UUID id) {
    super(clazz, id);
    this.actor = actor;
    this.fragments = fragments;
    validate();
  }

  public static Memory create(UUID actor) {
    return new Memory(actor, new ArrayList<>(), Memory.class, UUID.randomUUID());
  }

  public static Memory restore(UUID actor, List<MemoryFragment> fragments, UUID id) {
    return new Memory(actor, fragments, Memory.class, id);
  }

  public void addFragment(String text) {
    fragments.add(MemoryFragment.create(text));
  }

  public UUID getActor() {
    return actor;
  }

  public List<MemoryFragment> getFragments() {
    return List.copyOf(fragments);
  }

  @Override
  public final boolean validate() {
    return actor != null && fragments != null;
  }
}
