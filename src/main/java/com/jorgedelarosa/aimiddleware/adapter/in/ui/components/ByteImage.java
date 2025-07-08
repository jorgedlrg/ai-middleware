package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

/**
 * @author jorge
 */
//TODO it only supports png for now. fix this
public class ByteImage extends Image {

  public ByteImage(String label, byte[] data) {
    super(
        DownloadHandler.fromInputStream(
            (DownloadEvent downloadEvent) -> {
              try (OutputStream outputStream = downloadEvent.getOutputStream()) {
                outputStream.write(data);
              }
              return new DownloadResponse(
                  new ByteArrayInputStream(data), label, "image/png", data.length);
            }),
        label);
  }
}
