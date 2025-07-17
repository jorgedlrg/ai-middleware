package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutfitRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.OutfitMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteOutfitOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveOutfitOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class OutfitAdapter
    implements GetOutfitsOutPort, DeleteOutfitOutPort, GetOutfitByIdOutPort, SaveOutfitOutPort {

  private final OutfitRepository outfitRepository;

  @Override
  public List<Outfit> query() {
    return outfitRepository.findAll().stream().map(e -> OutfitMapper.INSTANCE.toDom(e)).toList();
  }

  @Override
  public void delete(Outfit outfit) {
    outfitRepository.deleteById(outfit.getId());
  }

  @Override
  public Optional<Outfit> query(UUID id) {
    return outfitRepository.findById(id).map(e -> OutfitMapper.INSTANCE.toDom(e));
  }

  @Override
  public void save(Outfit outfit) {
    outfitRepository.save(OutfitMapper.INSTANCE.toEntity(outfit));
  }
}
