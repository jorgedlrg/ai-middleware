package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutfitEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class OutfitMapperImpl implements OutfitMapper {

    @Override
    public GetOutfitsUseCase.OutfitDto toDto(Outfit dom) {
        if ( dom == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;

        id = dom.getId();
        name = dom.getName();
        description = dom.getDescription();

        GetOutfitsUseCase.OutfitDto outfitDto = new GetOutfitsUseCase.OutfitDto( id, name, description );

        return outfitDto;
    }

    @Override
    public GetOutfitDetailsUseCase.OutfitDto toDetailDto(Outfit dom) {
        if ( dom == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;

        id = dom.getId();
        name = dom.getName();
        description = dom.getDescription();

        GetOutfitDetailsUseCase.OutfitDto outfitDto = new GetOutfitDetailsUseCase.OutfitDto( id, name, description );

        return outfitDto;
    }

    @Override
    public OutfitEntity toEntity(Outfit dom) {
        if ( dom == null ) {
            return null;
        }

        OutfitEntity outfitEntity = new OutfitEntity();

        outfitEntity.setId( dom.getId() );
        outfitEntity.setName( dom.getName() );
        outfitEntity.setDescription( dom.getDescription() );

        return outfitEntity;
    }
}
