package com.jorgedelarosa.aimiddleware;

import com.jorgedelarosa.aimiddleware.adapter.out.embeddings.InfinispanClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AimiddlewareApplication {

  public static void main(String[] args) {
    SpringApplication.run(AimiddlewareApplication.class, args);

    InfinispanClient.mvp();
  }
}
