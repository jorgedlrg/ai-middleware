package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 *
 * @author jorge
 */
@Component
public class OpenRouterAdapter {

    public void test() {
        String API_KEY = "TODO";

        RestClient customClient = RestClient.builder()
                .baseUrl("https://openrouter.ai/api/v1/chat/completions")
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter()))
                .defaultHeader("Authorization", "Bearer " + API_KEY)
                .defaultHeader("HTTP-Referer", "jorgedelarosa.com")
                .defaultHeader("X-Title", "AI Middleware")
                .defaultHeader("Content-Type", "application/json")
                .build();

        OpenRouterChatCompletionResponse response = customClient.post().retrieve().body(OpenRouterChatCompletionResponse.class);
    }

}
