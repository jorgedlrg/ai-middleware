package com.jorgedelarosa.aimiddleware.adapter.in.web.v1.actor;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveActorUseCase;
import java.util.List;
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
@RequestMapping("/api/v1/actor/actors")
@RequiredArgsConstructor
public class ActorController {

  private final DeleteActorUseCase deleteActorUseCase;
  private final GetActorsUseCase getActorsUseCase;
  private final GetActorDetailsUseCase getActorDetailsUseCase;
  private final SaveActorUseCase saveActorUseCase;

  @DeleteMapping("/{actor}")
  public void deleteActor(UUID actor) {
    deleteActorUseCase.execute(new DeleteActorUseCase.Command(actor));
  }

  @GetMapping("/")
  public List<GetActorsUseCase.ActorDto> getActors() {
    return getActorsUseCase.execute();
  }

  @GetMapping("/{actor}/details")
  public GetActorDetailsUseCase.ActorDto getActorDetails(UUID actor) {
    return getActorDetailsUseCase.execute(new GetActorDetailsUseCase.Command(actor));
  }

  @PostMapping("/")
  public UUID saveActor(@RequestBody SaveActorUseCase.Command req) {
    return saveActorUseCase.execute(req);
  }
}
