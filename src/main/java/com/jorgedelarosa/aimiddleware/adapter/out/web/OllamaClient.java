package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OllamaClient {

  @Value("${ollama.host}")
  private String host;

  public static final String GEMMA3_12B = "gemma3:12b";

  // TODO: add text completion instead of chat completion. it might be better for formatted
  // processing.

  public OllamaChatResponse chatCompletion(OllamaChatRequest req) {

    String url = host + "/api/chat";

    log.debug("req: {}", req);
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

    return response;
  }
}
