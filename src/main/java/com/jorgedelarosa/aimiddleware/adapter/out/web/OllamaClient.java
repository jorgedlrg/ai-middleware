package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

/**
 * @author jorge
 */
@Slf4j
@RequiredArgsConstructor
public class OllamaClient {

  private final String host;

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
