package com.jorgedelarosa.aimiddleware.application.port.in;

/**
 * @author jorge
 */
public interface MachineInteractUseCase {

  public void execute(Command cmd);

  public record Command() {}
}
