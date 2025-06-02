package com.jorgedelarosa.aimiddleware.application.port.out;

/**
 * @author jorge
 */
public interface GenerateMachineInteractionOutPort {

  public MachineResponse execute(Command cmd);

  public record Command() {}
  ;

  // TODO refine this
  public record MachineResponse(String text) {}
  ;
}
