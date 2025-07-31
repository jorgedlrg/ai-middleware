package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import java.time.Duration;
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
public class OpenRouterClient {

  private final String apikey;

  private static final String URL = "https://openrouter.ai/api/v1/chat/completions";

  public OpenRouterChatCompletionResponse chatCompletion(Object req) {
    HttpComponentsClientHttpRequestFactory reqFactory =
        new HttpComponentsClientHttpRequestFactory();
    reqFactory.setConnectTimeout(Duration.ofSeconds(5));
    reqFactory.setConnectionRequestTimeout(Duration.ofSeconds(5));
    reqFactory.setReadTimeout(Duration.ofSeconds(30));

    log.info("req: {}", req);
    RestClient customClient =
        RestClient.builder()
            .baseUrl(URL)
            .requestFactory(reqFactory)
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
