package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class ActorMapperImpl implements ActorMapper {

    @Override
    public GetActorDetailsUseCase.ActorDto toDetailDto(Actor dom) {
        if ( dom == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String profile = null;
        String physicalDescription = null;
        Optional<GetActorDetailsUseCase.MindDto> mind = null;
        Optional<UUID> currentOutfit = null;

        id = dom.getId();
        name = dom.getName();
        profile = dom.getProfile();
        physicalDescription = dom.getPhysicalDescription();
        mind = map( dom.getMind() );
        currentOutfit = dom.getCurrentOutfit();

        GetActorDetailsUseCase.ActorDto actorDto = new GetActorDetailsUseCase.ActorDto( id, name, profile, physicalDescription, mind, currentOutfit );

        return actorDto;
    }

    @Override
    public GetActorsUseCase.ActorDto toDto(Actor dom) {
        if ( dom == null ) {
            return null;
        }

        UUID id = null;
        String name = null;

        id = dom.getId();
        name = dom.getName();

        GetActorsUseCase.ActorDto actorDto = new GetActorsUseCase.ActorDto( id, name );

        return actorDto;
    }

    @Override
    public MindEntity toEntity(Mind dom) {
        if ( dom == null ) {
            return null;
        }

        MindEntity mindEntity = new MindEntity();

        mindEntity.setActor( dom.getId() );
        mindEntity.setPersonality( dom.getPersonality() );

        return mindEntity;
    }
}
