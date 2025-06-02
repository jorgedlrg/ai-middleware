package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatResponse;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * @author jorge
 */
@Component
public class OllamaClient {

  private final Logger logger = LoggerFactory.getLogger(OpenRouterClient.class);

  @Value("${ollama.host}")
  private String host;

  public String MODEL = "gemma3:12b";

  // TODO: add text completion instead of chat completion. it might be better for formatted
  // processing.

  public OllamaChatResponse chatCompletion(OllamaChatRequest req) {

    String url = host + "/api/chat";

    logger.info("req: {}", req);
    RestClient customClient =
        RestClient.builder()
            .baseUrl(url)
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .messageConverters(
                converters -> converters.add(new MappingJackson2HttpMessageConverter()))
            .build();

    OllamaChatResponse response =
        customClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(req)
            .retrieve()
            .body(OllamaChatResponse.class);

    logger.info("res: {}", response);

    return response;
  }
}
