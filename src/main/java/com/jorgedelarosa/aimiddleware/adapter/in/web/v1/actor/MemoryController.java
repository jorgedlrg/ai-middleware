package com.jorgedelarosa.aimiddleware.adapter.in.web.v1.actor;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteMemoryFragmentUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveMemoryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorge
 */
@RestController
@RequestMapping("/api/v1/actor/memories")
@RequiredArgsConstructor
public class MemoryController {

  private final DeleteMemoryFragmentUseCase deleteMemoryFragmentUseCase;
  private final GetMemoryUseCase getMemoryUseCase;
  private final SaveMemoryUseCase saveMemoryUseCase;

  @DeleteMapping("/{actor}/fragment/{fragment}")
  public void deleteActor(UUID actor, UUID fragment) {
    deleteMemoryFragmentUseCase.execute(new DeleteMemoryFragmentUseCase.Command(actor, fragment));
  }

  @GetMapping("/{actor}")
  public GetMemoryUseCase.MemoryDto getActorDetails(UUID actor) {
    return getMemoryUseCase.execute(new GetMemoryUseCase.Command(actor));
  }

  @PostMapping("/")
  public UUID saveActor(@RequestBody SaveMemoryUseCase.Command req) {
    return saveMemoryUseCase.execute(req);
  }
}
