package com.jorgedelarosa.aimiddleware.adapter.in.web;

import com.jorgedelarosa.aimiddleware.adapter.in.web.dto.InteractUserReq;
import com.jorgedelarosa.aimiddleware.application.port.in.MachineInteractUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.RetrieveSessionInteractionsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.RetrieveSessionInteractionsUseCase.InteractionDto;
import com.jorgedelarosa.aimiddleware.application.port.in.UserInteractUseCase;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorge
 */

// API First is a big Domain Driven Design ANTIPATTERN, although very popular in LinkedIn. I'll deal
// with the API when the core model and functionality is properly designed and stabilized.
// This API is only for development purposes.
@RestController
@RequestMapping("/api/v0/interact")
@AllArgsConstructor
public class InteractController {

  private final MachineInteractUseCase machineInteractUseCase;
  private final UserInteractUseCase userInteractUseCase;
  private final RetrieveSessionInteractionsUseCase retrieveSessionInteractionsUseCase;

  private final UUID SESSION = UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a");

  @PostMapping("/user")
  public void interactUser(@RequestBody InteractUserReq req) {
    userInteractUseCase.execute(
        new UserInteractUseCase.Command(
            SESSION, UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"), req.text()));
  }

  @PostMapping("/user/{role}")
  public void interactUser(@RequestBody InteractUserReq req, UUID role) {
    userInteractUseCase.execute(new UserInteractUseCase.Command(SESSION, role, req.text()));
  }

  @PostMapping("/machine")
  public void interactMachine() {
    machineInteractUseCase.execute(
        new MachineInteractUseCase.Command(
            SESSION, UUID.fromString("655cfb3d-c740-48d2-ab4f-51e391c4deaf")));
  }

  @PostMapping("/machine/{role}")
  public void interactMachineRole(UUID role) {
    machineInteractUseCase.execute(new MachineInteractUseCase.Command(SESSION, role));
  }

  @GetMapping("/messages")
  public List<InteractionDto> messages() {
    return retrieveSessionInteractionsUseCase.execute(
        new RetrieveSessionInteractionsUseCase.Command(SESSION));
  }
}
