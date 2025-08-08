package com.jorgedelarosa.aimiddleware.infrastructure;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class H2DataProtection {

  private final JdbcTemplate jdbcTemplate;

  @Scheduled(fixedDelay = 6000)
  public void scheduledCheckpoint() {
    try {
      jdbcTemplate.execute("CHECKPOINT SYNC");
      log.debug("H2 checkpoint executed");
    } catch (DataAccessException e) {
      log.error("Error when creating an H2 checkpoint", e);
    }
  }

  @PreDestroy
  public void finalCheckpoint() {
    try {
      log.info("Executing final checkpoint...");
      jdbcTemplate.execute("CHECKPOINT SYNC");
      jdbcTemplate.execute("SHUTDOWN COMPACT");
      log.info("H2 FINAL checkpoint executed. Data should be safe!");
    } catch (DataAccessException e) {
      log.error("Error when creating the final H2 checkpoint", e);
    }
  }
}
