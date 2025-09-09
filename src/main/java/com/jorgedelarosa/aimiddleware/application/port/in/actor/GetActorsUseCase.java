package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorsUseCase {

  public List<ActorDto> execute();


  public record ActorDto(UUID id, String name, byte[] portrait) {}
}
