package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
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
public class OpenRouterClient {

  @Value("${openrouter.apikey}")
  private String apikey;

  @Value("${openrouter.model}")
  private String model;

  private static final String URL = "https://openrouter.ai/api/v1/chat/completions";

  public OpenRouterChatCompletionResponse chatCompletion(OpenRouterChatCompletionRequest req) {

    log.debug("req: {}", req);
    RestClient customClient =
        RestClient.builder()
            .baseUrl(URL)
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .messageConverters(
                converters -> converters.add(new MappingJackson2HttpMessageConverter()))
            .defaultHeader("Authorization", "Bearer " + apikey)
            .defaultHeader("HTTP-Referer", "jorgedelarosa.com")
            .defaultHeader("X-Title", "AI Middleware")
            .build();

    OpenRouterChatCompletionResponse response =
        customClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(req)
            .retrieve()
            .body(OpenRouterChatCompletionResponse.class);

    return response;
  }

  public String getModel() {
    return model;
  }
}
