package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.OutfitMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetOutfitDetailsUseCaseImpl implements GetOutfitDetailsUseCase {

  private final GetOutfitByIdOutPort getOutfitByIdOutPort;

  @Override
  public OutfitDto execute(Command cmd) {
    return getOutfitByIdOutPort
        .query(cmd.id())
        .map(e -> OutfitMapper.INSTANCE.toDetailDto(e))
        .orElseThrow();
  }
}
