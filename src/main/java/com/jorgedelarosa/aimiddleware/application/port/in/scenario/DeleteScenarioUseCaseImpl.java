package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteScenarioOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.scenario.ScenarioDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteScenarioUseCaseImpl implements DeleteScenarioUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final DeleteScenarioOutPort deleteScenarioOutPort;
  private final PublishDomainEventOutPort publishDomainEventOutPort;

  @Override
  public void execute(Command cmd) {
    Scenario scenario = getScenarioByIdOutPort.query(cmd.scenarioId()).orElseThrow();
    deleteScenarioOutPort.delete(scenario);
    publishDomainEventOutPort.publishDomainEvent(
        new ScenarioDeletedEvent(scenario.getAggregateId(), 1l));
  }
}
