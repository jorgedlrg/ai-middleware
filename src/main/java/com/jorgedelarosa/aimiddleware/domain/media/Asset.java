package com.jorgedelarosa.aimiddleware.domain.media;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.UUID;

/**
 * @author jorge
 */
public class Asset extends AggregateRoot {

  private final byte[] data;
  private final MediaType supportedMediaType;

  private Asset(UUID id, byte[] data, MediaType supportedMediaType) {
    super(Asset.class, id);
    this.data = data;
    this.supportedMediaType = supportedMediaType;
    validate();
  }

  public static Asset restore(UUID id, byte[] data, MediaType supportedMediaType) {
    return new Asset(id, data, supportedMediaType);
  }

  public static Asset create(byte[] data, MediaType supportedMediaType) {
    return new Asset(UUID.randomUUID(), data, supportedMediaType);
  }

  @Override
  public final boolean validate() {
    return data.length > 0 && supportedMediaType != null;
  }

  public byte[] getData() {
    return data;
  }

  public MediaType getSupportedMediaType() {
    return supportedMediaType;
  }

  public enum MediaType {
    PNG("image/png"),
    WEBP("image/webp"),
    MP4("video/mp4");

    private final String value;

    private MediaType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
