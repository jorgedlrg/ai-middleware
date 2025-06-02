package com.jorgedelarosa.aimiddleware.adapter.in.web;

import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractMachineRes;
import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractUserReq;
import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractUserRes;
import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping("/interact/user")
  public InteractUserRes interactUser(@RequestBody InteractUserReq req) {
    userInteractUseCase.execute(new UserInteractUseCase.Command(req.text()));

    return new InteractUserRes("ok");
  }

  @PostMapping("/interact/machine")
  public InteractMachineRes interactMachine() {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command());

    return new InteractMachineRes("ok");
  }
}
