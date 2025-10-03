package com.jorgedelarosa.aimiddleware.adapter.in.web.v1.actor;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteOutfitUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveOutfitUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: These controllers should have their own DTOs, in order to not break API contract
// when changing any of the use cases, and then just use mappers.

/**
 * @author jorge
 */
@RestController
@RequestMapping("/api/v1/actor/outfits")
@RequiredArgsConstructor
public class OutfitController {
  private final DeleteOutfitUseCase deleteOutfitUseCase;
  private final GetOutfitsUseCase getOutfitsUseCase;
  private final GetOutfitDetailsUseCase getOutfitDetailsUseCase;
  private final SaveOutfitUseCase saveOutfitUseCase;

  @DeleteMapping("/{outfit}")
  public void deleteOutfit(UUID outfit) {
    deleteOutfitUseCase.execute(new DeleteOutfitUseCase.Command(outfit));
  }

  @GetMapping("/")
  public List<GetOutfitsUseCase.OutfitDto> getOutfits() {
    return getOutfitsUseCase.execute();
  }

  @GetMapping("/{outfit}/details")
  public GetOutfitDetailsUseCase.OutfitDto getOutfitDetails(UUID outfit) {
    return getOutfitDetailsUseCase.execute(new GetOutfitDetailsUseCase.Command(outfit));
  }

  @PostMapping("/")
  public UUID saveOutfit(@RequestBody SaveOutfitUseCase.Command req) {
    return saveOutfitUseCase.execute(req);
  }
}
