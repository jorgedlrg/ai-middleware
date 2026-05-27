package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

public interface GenerateActorPortraitUseCase {

  String execute(Command cmd);

  record Command(UUID actorId) {}
}
