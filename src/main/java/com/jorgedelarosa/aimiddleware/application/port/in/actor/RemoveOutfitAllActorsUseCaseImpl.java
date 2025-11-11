package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByCurrentOutfitOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class RemoveOutfitAllActorsUseCaseImpl implements RemoveOutfitAllActorsUseCase {

  private final GetActorListByCurrentOutfitOutPort getActorListByCurrentOutfitOutPort;
  private final SaveActorOutPort saveActorOutPort;

  @Override
  public void execute(Command cmd) {
    List<Actor> actors = getActorListByCurrentOutfitOutPort.queryActors(cmd.outfit());
    for (Actor actor : actors) {
      actor.chooseOutfit(null);
      saveActorOutPort.save(actor);
    }
  }
}
