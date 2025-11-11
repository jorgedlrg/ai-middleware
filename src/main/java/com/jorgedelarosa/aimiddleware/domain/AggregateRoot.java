package com.jorgedelarosa.aimiddleware.domain;

import java.util.UUID;

/**
 * @author jorge
 */
public abstract class AggregateRoot extends Entity {

  protected final AggregateId aggregateId;

  public AggregateRoot(Class clazz, UUID id) {
    super(id);
    aggregateId = new AggregateId(clazz, id);
  }

  public AggregateId getAggregateId() {
    return aggregateId;
  }

  public static class AggregateId {
    private final Class clazz;
    private final UUID id;

    public AggregateId(Class clazz, UUID id) {
      this.clazz = clazz;
      this.id = id;
    }

    public Class getClazz() {
      return clazz;
    }

    public UUID getId() {
      return id;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("urn:");
      sb.append(clazz.getName()).append(":");
      sb.append(id.toString());
      return sb.toString();
    }
  }
}
