package com.jorgedelarosa.aimiddleware.adapter.in.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.OpenRouterAdapter;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorge
 */
@RestController
@AllArgsConstructor
public class InfoController {

  private OpenRouterAdapter orAdapter;

  @GetMapping("/info")
  public String info(@RequestParam(value = "echo", defaultValue = "hello") String echo) {
    return echo;
  }

  @GetMapping("/chat")
  public OpenRouterChatCompletionResponse chat() {
    return orAdapter.test();
  }
}
