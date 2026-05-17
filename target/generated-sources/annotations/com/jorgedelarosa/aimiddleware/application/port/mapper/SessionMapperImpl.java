package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.EditSessionUseCase;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class SessionMapperImpl implements SessionMapper {

    @Override
    public Performance toDom(CreateSessionUseCase.PerformanceDto dto) {
        if ( dto == null ) {
            return null;
        }

        UUID actor = null;
        UUID role = null;

        actor = dto.actor();
        role = dto.role();

        Performance performance = new Performance( actor, role );

        return performance;
    }

    @Override
    public Performance toDom(EditSessionUseCase.PerformanceDto dto) {
        if ( dto == null ) {
            return null;
        }

        UUID actor = null;
        UUID role = null;

        actor = dto.actor();
        role = dto.role();

        Performance performance = new Performance( actor, role );

        return performance;
    }

    @Override
    public SessionEntity toEntity(Session session) {
        if ( session == null ) {
            return null;
        }

        SessionEntity sessionEntity = new SessionEntity();

        sessionEntity.setId( session.getId() );
        sessionEntity.setScenario( session.getScenario() );
        sessionEntity.setCurrentContext( session.getCurrentContext() );
        sessionEntity.setLocale( session.getLocale() );
        sessionEntity.setLastInteraction( map( session.getLastInteraction() ) );

        return sessionEntity;
    }
}
