package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteOutfitOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class DeleteOutfitUseCaseImpl implements DeleteOutfitUseCase {

  private final GetOutfitByIdOutPort getOutfitByIdOutPort;
  private final DeleteOutfitOutPort deleteOutfitOutPort;

  @Override
  public void execute(Command cmd) {
    Outfit outfit = getOutfitByIdOutPort.query(cmd.id()).orElseThrow();
    deleteOutfitOutPort.delete(outfit);
  }
}
