package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class ScenarioAdapter implements GetScenarioByIdOutPort {

  private final ScenarioRepository scenarioRepository;
  private final ContextRepository contextRepository;

  @Override
  public Optional<Scenario> query(UUID id) {
    Optional<ScenarioEntity> scenarioEntity = scenarioRepository.findById(id);
    if (scenarioEntity.isPresent()) {
      List<Context> contexts =
          contextRepository.findAllByScenario(id).stream()
              .map((e) -> ContextEntityMapper.INSTANCE.toDom(e))
              .toList();
      return Optional.of(Scenario.restore(scenarioEntity.get().getId(), contexts));
    } else {
      return Optional.empty();
    }
  }

  @Mapper
  public interface ContextEntityMapper {
    ContextEntityMapper INSTANCE = Mappers.getMapper(ContextEntityMapper.class);

    default Context toDom(ContextEntity entity) {
      return Context.restore(entity.getId(), entity.getName(), entity.getPhysicalDescription());
    }
  }
}
