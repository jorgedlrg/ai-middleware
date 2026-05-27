package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetScenariosUseCase {

  public List<ScenarioDto> execute(Command cmd);

  // TODO: find by user
  public record Command() {}

  public record ScenarioDto(
      UUID id,
      String name,
      List<String> contextNames,
      List<String> roleNames,
      int introductionCount) {}
}
