package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.ComfyUiPromptRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.ComfyUiPromptResponse;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.PromptHistory;
import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class ComfyUiClient {

  private final String host;

  public ComfyUiClient(String host) {
    this.host = "http://" + host;
  }

  public String queuePrompt(Map<String, Object> prompt, String clientId) {
    String url = host + "/prompt";
    ComfyUiPromptRequest request = new ComfyUiPromptRequest(prompt, clientId);
    log.info("Queueing prompt to {}: {}", url, request);
    RestClient client = RestClient.create();
    ComfyUiPromptResponse response =
        client
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(ComfyUiPromptResponse.class);
    log.info("Prompt queued: {}", response);
    if (response.nodeErrors() != null && !response.nodeErrors().isEmpty()) {
      throw new RuntimeException("Prompt validation failed: " + response.nodeErrors());
    }
    return response.promptId();
  }

  public Map<String, PromptHistory> getHistory(String promptId) {
    String url = host + "/history/" + promptId;
    log.info("Fetching history from {}", url);
    RestClient client = RestClient.create();
    Map<String, PromptHistory> response =
        client
            .get()
            .uri(url)
            .retrieve()
            .body(new ParameterizedTypeReference<Map<String, PromptHistory>>() {});
    log.info("History response: {}", response);
    return response;
  }

  public byte[] getImage(String filename, String subfolder, String type) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromPath("/view")
            .queryParam("filename", filename)
            .queryParam("subfolder", subfolder)
            .queryParam("type", type);
    String url = host + builder.build().toUriString();
    log.info("Fetching image from {}", url);
    RestClient client = RestClient.create();
    return client.get().uri(URI.create(url)).retrieve().body(byte[].class);
  }
}