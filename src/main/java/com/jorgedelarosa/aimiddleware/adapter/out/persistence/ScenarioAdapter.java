package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.ScenarioEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.ScenarioRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class ScenarioAdapter implements GetScenarioByIdOutPort {

  private final ScenarioRepository scenarioRepository;

  @Override
  public Optional<Scenario> query(UUID id) {
    Optional<ScenarioEntity> scenarioEntity = scenarioRepository.findById(id);
    if (scenarioEntity.isPresent()) {
      return Optional.of(Scenario.restore(scenarioEntity.get().getId()));
    } else {
      return Optional.empty();
    }
  }
}
