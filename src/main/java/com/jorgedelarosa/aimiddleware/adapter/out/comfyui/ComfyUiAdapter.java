package com.jorgedelarosa.aimiddleware.adapter.out.comfyui;

import com.jorgedelarosa.aimiddleware.adapter.in.message.ComfyUiWebSocketListener;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.filesystem.AssetRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.web.ComfyUiClient;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.PromptHistory;
import com.jorgedelarosa.aimiddleware.application.port.out.ComfyUiOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ComfyUiAdapter implements ComfyUiOutPort {

  private static final UUID USER_ID = UUID.fromString("857fa610-b987-454c-96c3-bbf5354f13a0");

  private final AssetRepository assetRepository;
  private final GetUserByIdOutPort getUserByIdOutPort;

  private ComfyUiClient client;
  private ComfyUiWebSocketListener webSocketListener;
  private String clientId;
  private volatile boolean initialized = false;

  public ComfyUiAdapter(AssetRepository assetRepository, GetUserByIdOutPort getUserByIdOutPort) {
    this.assetRepository = assetRepository;
    this.getUserByIdOutPort = getUserByIdOutPort;
  }

  private void initializeIfNeeded() {
    if (!initialized) {
      synchronized (this) {
        if (!initialized) {
          User user =
              getUserByIdOutPort
                  .query(USER_ID)
                  .orElseThrow(() -> new RuntimeException("User not found"));
          String comfyUiHost = user.getSettings().getComfyUiHost();

          this.client = new ComfyUiClient(comfyUiHost);
          this.clientId = UUID.randomUUID().toString();

          this.webSocketListener =
              new ComfyUiWebSocketListener(
                  comfyUiHost,
                  clientId,
                  this::onPromptComplete,
                  this::onPromptError);
          this.webSocketListener.start();
          initialized = true;
        }
      }
    }
  }

  @Override
  public String queuePrompt(Map<String, Object> workflow, String relativePath) {
    initializeIfNeeded();
    String promptId = client.queuePrompt(workflow, clientId);
    promptToRelativePath.put(promptId, relativePath);
    log.info("Queued prompt {} with relativePath {}", promptId, relativePath);
    return promptId;
  }

  private final java.util.concurrent.ConcurrentHashMap<String, String> promptToRelativePath =
      new java.util.concurrent.ConcurrentHashMap<>();

  private void onPromptComplete(String promptId) {
    String relativePath = promptToRelativePath.remove(promptId);
    if (relativePath == null) {
      log.warn("No relativePath found for prompt {}", promptId);
      return;
    }
    try {
      Map<String, PromptHistory> history = client.getHistory(promptId);
      if (history == null || history.isEmpty()) {
        log.warn("No history found for prompt {}", promptId);
        return;
      }
      String parentDir =
          Paths.get(relativePath).getParent() != null
              ? Paths.get(relativePath).getParent().toString()
              : "";
      String filename = Paths.get(relativePath).getFileName().toString();
      int index = 0;
      for (var entry : history.entrySet()) {
        PromptHistory promptHistory = entry.getValue();
        if (promptHistory.outputs() != null) {
          for (var outputEntry : promptHistory.outputs().entrySet()) {
            PromptHistory.OutputNode outputNode = outputEntry.getValue();
            if (outputNode.images() != null) {
              for (PromptHistory.ImageOutput image : outputNode.images()) {
                byte[] imageData =
                    client.getImage(image.filename(), image.subfolder(), image.type());
                String finalFilename = index == 0 ? filename : generateFilename(filename, index);
                assetRepository.save(parentDir, finalFilename, imageData);
                log.info("Saved image to {}/{}", parentDir, finalFilename);
                index++;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("Failed to process prompt completion for {}", promptId, e);
    }
  }

  private void onPromptError(String promptId) {
    promptToRelativePath.remove(promptId);
    log.warn("Prompt {} failed", promptId);
  }

  private String generateFilename(String baseFilename, int index) {
    int dotIndex = baseFilename.lastIndexOf('.');
    if (dotIndex > 0) {
      String name = baseFilename.substring(0, dotIndex);
      String ext = baseFilename.substring(dotIndex);
      return name + "_" + index + ext;
    }
    return baseFilename + "_" + index;
  }
}