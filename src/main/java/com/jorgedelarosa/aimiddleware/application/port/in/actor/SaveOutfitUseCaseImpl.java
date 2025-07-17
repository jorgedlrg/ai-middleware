package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveOutfitOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class SaveOutfitUseCaseImpl implements SaveOutfitUseCase {

  private final GetOutfitByIdOutPort getOutfitByIdOutPort;
  private final SaveOutfitOutPort saveOutfitOutPort;

  @Override
  public UUID execute(Command cmd) {
    Outfit outfit;
    if (cmd.id() == null) {
      // CREATE
      outfit = Outfit.create(cmd.name(), cmd.description());
    } else {
      // UPDATE
      outfit = getOutfitByIdOutPort.query(cmd.id()).orElseThrow();

      outfit.setName(cmd.name());
      outfit.setDescription(cmd.description());
    }

    saveOutfitOutPort.save(outfit);
    return outfit.getId();
  }
}
