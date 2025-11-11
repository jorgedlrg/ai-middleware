package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.OutfitMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitsOutPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetOutfitsUseCaseImpl implements GetOutfitsUseCase {

  private final GetOutfitsOutPort getOutfitsOutPort;

  @Override
  public List<OutfitDto> execute() {
    return getOutfitsOutPort.query().stream().map(e -> OutfitMapper.INSTANCE.toDto(e)).toList();
  }
}
