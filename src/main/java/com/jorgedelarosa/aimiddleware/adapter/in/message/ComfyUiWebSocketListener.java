package com.jorgedelarosa.aimiddleware.adapter.in.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.ComfyUiWebSocketMessage;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class ComfyUiWebSocketListener extends TextWebSocketHandler {

  private final String host;
  private final String clientId;
  private final Consumer<String> onComplete;
  private final Consumer<String> onError;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private WebSocketSession session;
  private WebSocketConnectionManager connectionManager;
  private static final int RECONNECT_DELAY_MS = 30_000;

  public ComfyUiWebSocketListener(
      String host, String clientId, Consumer<String> onComplete, Consumer<String> onError) {
    this.host = host;
    this.clientId = clientId;
    this.onComplete = onComplete;
    this.onError = onError;
  }

  public void start() {
    String uri = "ws://" + host + "/ws?clientId=" + clientId;
    log.info("Connecting to ComfyUI WebSocket: {}", uri);
    connectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), this, uri);
    connectionManager.start();
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    this.session = session;
    log.info("WebSocket connected: {}", session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    try {
      ComfyUiWebSocketMessage msg =
          objectMapper.readValue(message.getPayload(), ComfyUiWebSocketMessage.class);
      log.debug("WebSocket message received: {}", msg);
      if ("executing".equals(msg.type())) {
        if (msg.data().node() == null && msg.data().promptId() != null) {
          log.info("Prompt {} completed", msg.data().promptId());
          onComplete.accept(msg.data().promptId());
        }
      } else if ("execution_error".equals(msg.type())) {
        log.warn("Execution error for prompt {}: {}", msg.data().promptId(), msg.data());
        onError.accept(msg.data().promptId());
      }
    } catch (Exception e) {
      log.error("Failed to parse WebSocket message", e);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    log.warn("WebSocket closed: {} - {}", status.getCode(), status.getReason());
    scheduleReconnect();
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    log.error("WebSocket transport error", exception);
    scheduleReconnect();
  }

  private void scheduleReconnect() {
    new Thread(
            () -> {
              try {
                Thread.sleep(RECONNECT_DELAY_MS);
                if (session == null || !session.isOpen()) {
                  log.info("Attempting to reconnect WebSocket...");
                  start();
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            })
        .start();
  }
}