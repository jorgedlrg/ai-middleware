package com.jorgedelarosa.aimiddleware.adapter.in.web.v1.actor;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.filesystem.AssetRepository;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveActorUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: These controllers should have their own DTOs, in order to not break API contract
// when changing any of the use cases, and then just use mappers.

@RestController
@RequestMapping("/api/v1/actor/actors")
@RequiredArgsConstructor
public class ActorController {

  private final DeleteActorUseCase deleteActorUseCase;
  private final GetActorsUseCase getActorsUseCase;
  private final GetActorDetailsUseCase getActorDetailsUseCase;
  private final SaveActorUseCase saveActorUseCase;
  private final AssetRepository assetRepository;

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

  @GetMapping(value = "/{actor}/portrait", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<ByteArrayResource> portrait(@PathVariable UUID actor) {
    byte[] data = assetRepository.load("actors/" + actor + "/portrait.png");
    ByteArrayResource resource = new ByteArrayResource(data);
    return ResponseEntity.ok(resource);
  }

  @PostMapping("/")
  public UUID saveActor(@RequestBody SaveActorUseCase.Command req) {
    return saveActorUseCase.execute(req);
  }
}
