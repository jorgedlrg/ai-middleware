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

  public String MODEL = "google/gemma-3-12b-it";

  public OpenRouterChatCompletionResponse chatCompletion(OpenRouterChatCompletionRequest req) {

    String url = "https://openrouter.ai/api/v1/chat/completions";

    log.info("req: {}", req);
    RestClient customClient =
        RestClient.builder()
            .baseUrl(url)
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

    log.info("res: {}", response);

    return response;
  }
}
