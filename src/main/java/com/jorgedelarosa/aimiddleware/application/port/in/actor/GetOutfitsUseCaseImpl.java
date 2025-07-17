package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.OutfitMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitsOutPort;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetOutfitsUseCaseImpl implements GetOutfitsUseCase {

  private final GetOutfitsOutPort getOutfitsOutPort;

  @Override
  public List<OutfitDto> execute(Command cmd) {
    return getOutfitsOutPort.query().stream().map(e -> OutfitMapper.INSTANCE.toDto(e)).toList();
  }
}
