package com.jorgedelarosa.aimiddleware.application.port.out;

import java.util.UUID;

public interface SaveActorPortraitOutPort {

  public void savePortrait(UUID actorId, byte[] portrait);
}