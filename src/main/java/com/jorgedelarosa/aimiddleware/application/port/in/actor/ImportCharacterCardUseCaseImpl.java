package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ImportCharacterCardUseCaseImpl implements ImportCharacterCardUseCase {

  private final SaveActorOutPort saveActorOutPort;
  private final SaveScenarioOutPort saveScenarioOutPort;

  @Override
  public UUID execute(Command cmd) {
    Actor actor = Actor.create(cmd.name(), cmd.description(), cmd.description(), cmd.personality());
    saveActorOutPort.save(actor, cmd.portrait());

    if (cmd.scenario() != null && !cmd.scenario().isBlank()) {
      Scenario scenario = Scenario.create(actor.getName() + " - Scenario", "This is a roleplay scenario.");
      scenario.addNewContext("Default", cmd.scenario());
      scenario.addNewRole("char", "The character acting in this roleplay");
      scenario.addNewRole("user", "The user interacting with the character");

      Context sceneContext = scenario.getContexts().getFirst();
      Role savedCharRole =
          scenario.getRoles().stream()
              .filter(r -> r.getName().equals("char"))
              .findFirst()
              .orElseThrow();

      if (cmd.first_mes() != null && !cmd.first_mes().isBlank()) {
        scenario.addNewIntroduction(
            cmd.first_mes(), Optional.empty(), Optional.empty(), savedCharRole, sceneContext);
      }

      if (cmd.alternate_greetings() != null) {
        for (String greeting : cmd.alternate_greetings()) {
          if (greeting != null && !greeting.isBlank()) {
            scenario.addNewIntroduction(
                greeting, Optional.empty(), Optional.empty(), savedCharRole, sceneContext);
          }
        }
      }

      saveScenarioOutPort.save(scenario);
    }

    return actor.getId();
  }
}
