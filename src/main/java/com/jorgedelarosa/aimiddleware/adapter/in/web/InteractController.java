package com.jorgedelarosa.aimiddleware.adapter.in.web;

import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorge
 */

// Non final API. for dev purposes
@RestController
@AllArgsConstructor
public class InteractController {

  private final MachineInteractUseCase machineInteractUseCase;
  private final UserInteractUseCase userInteractUseCase;

  @GetMapping("/interact/user")
  public String interactUser() {
    userInteractUseCase.execute(new UserInteractUseCase.Command("hello"));

    return "ok";
  }

  @GetMapping("/interact/machine")
  public String interactMachine() {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command());

    return "ok";
  }
}
