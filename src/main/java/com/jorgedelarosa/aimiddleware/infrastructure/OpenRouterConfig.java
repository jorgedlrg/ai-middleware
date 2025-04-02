package com.jorgedelarosa.aimiddleware.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jorge
 */
@Configuration
@ConfigurationProperties(prefix = "openrouter")
public class OpenRouterConfig {

  private String apikey;

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }
}
