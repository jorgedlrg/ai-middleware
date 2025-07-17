package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutfitEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface OutfitMapper {
  OutfitMapper INSTANCE = Mappers.getMapper(OutfitMapper.class);

  GetOutfitsUseCase.OutfitDto toDto(Outfit dom);

  GetOutfitDetailsUseCase.OutfitDto toDetailDto(Outfit dom);

  default Outfit toDom(OutfitEntity e) {
    return Outfit.restore(e.getId(), e.getName(), e.getDescription());
  }

  OutfitEntity toEntity(Outfit dom);
}
