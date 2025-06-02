package com.jorgedelarosa.aimiddleware.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jorge
 */
@Configuration
@ConfigurationProperties(prefix = "ollama")
@Getter
@Setter
public class OllamaConfig {

  private String host;
}
