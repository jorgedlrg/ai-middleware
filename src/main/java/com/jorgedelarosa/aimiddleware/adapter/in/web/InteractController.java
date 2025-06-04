package com.jorgedelarosa.aimiddleware.adapter.in.web;

import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractMachineRes;
import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractUserReq;
import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractUserRes;
import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorge
 */

// API First is a big Domain Driven Design ANTIPATTERN, although very popular in LinkedIn. I'll deal
// with the API when the core model and functionality is properly designed and stabilized.
@RestController
@RequestMapping("/api/v0/interact")
@AllArgsConstructor
public class InteractController {

  private final MachineInteractUseCase machineInteractUseCase;
  private final UserInteractUseCase userInteractUseCase;

  @PostMapping("/user")
  public InteractUserRes interactUser(@RequestBody InteractUserReq req) {
    userInteractUseCase.execute(new UserInteractUseCase.Command(req.text()));

    return new InteractUserRes("ok");
  }

  @PostMapping("/machine")
  public InteractMachineRes interactMachine() {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command());

    return new InteractMachineRes("ok");
  }
}
