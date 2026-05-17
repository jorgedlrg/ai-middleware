package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MemoryFragmentEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class MemoryMapperImpl implements MemoryMapper {

    @Override
    public MemoryFragmentEntity map(MemoryFragment dom, UUID owner) {
        if ( dom == null && owner == null ) {
            return null;
        }

        MemoryFragmentEntity memoryFragmentEntity = new MemoryFragmentEntity();

        if ( dom != null ) {
            memoryFragmentEntity.setId( dom.getId() );
            memoryFragmentEntity.setTimestamp( map( dom.getTimestamp() ) );
            memoryFragmentEntity.setText( dom.getText() );
            memoryFragmentEntity.setEnabled( dom.isEnabled() );
        }
        memoryFragmentEntity.setOwner( owner );

        return memoryFragmentEntity;
    }

    @Override
    public GetMemoryUseCase.MemoryDto map(Memory dom) {
        if ( dom == null ) {
            return null;
        }

        UUID actorId = null;
        List<GetMemoryUseCase.MemoryFragmentDto> fragments = null;

        actorId = dom.getActor();
        fragments = memoryFragmentListToMemoryFragmentDtoList( dom.getFragments() );

        GetMemoryUseCase.MemoryDto memoryDto = new GetMemoryUseCase.MemoryDto( actorId, fragments );

        return memoryDto;
    }

    protected GetMemoryUseCase.MemoryFragmentDto memoryFragmentToMemoryFragmentDto(MemoryFragment memoryFragment) {
        if ( memoryFragment == null ) {
            return null;
        }

        UUID id = null;
        String text = null;
        Instant timestamp = null;
        boolean enabled = false;

        id = memoryFragment.getId();
        text = memoryFragment.getText();
        timestamp = memoryFragment.getTimestamp();
        enabled = memoryFragment.isEnabled();

        GetMemoryUseCase.MemoryFragmentDto memoryFragmentDto = new GetMemoryUseCase.MemoryFragmentDto( id, text, timestamp, enabled );

        return memoryFragmentDto;
    }

    protected List<GetMemoryUseCase.MemoryFragmentDto> memoryFragmentListToMemoryFragmentDtoList(List<MemoryFragment> list) {
        if ( list == null ) {
            return null;
        }

        List<GetMemoryUseCase.MemoryFragmentDto> list1 = new ArrayList<GetMemoryUseCase.MemoryFragmentDto>( list.size() );
        for ( MemoryFragment memoryFragment : list ) {
            list1.add( memoryFragmentToMemoryFragmentDto( memoryFragment ) );
        }

        return list1;
    }
}
