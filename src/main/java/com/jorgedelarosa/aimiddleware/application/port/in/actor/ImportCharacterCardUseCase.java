package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.List;
import java.util.UUID;

public interface ImportCharacterCardUseCase {
  UUID execute(Command cmd);

  record Command(
      String name,
      String description,
      String personality,
      String scenario,
      String first_mes,
      List<String> alternate_greetings,
      byte[] portrait) {}
}
