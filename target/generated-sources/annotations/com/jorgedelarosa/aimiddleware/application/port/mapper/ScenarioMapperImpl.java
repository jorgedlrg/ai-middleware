package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ContextEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.IntroductionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.RoleEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ScenarioEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Introduction;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class ScenarioMapperImpl implements ScenarioMapper {

    @Override
    public GetScenarioDetailsUseCase.ScenarioDto toDetailsDto(Scenario dom) {
        if ( dom == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        List<GetScenarioDetailsUseCase.ContextDto> contexts = null;
        List<GetScenarioDetailsUseCase.RoleDto> roles = null;
        List<GetScenarioDetailsUseCase.IntroductionDto> introductions = null;

        id = dom.getId();
        name = dom.getName();
        description = dom.getDescription();
        contexts = contextListToContextDtoList( dom.getContexts() );
        roles = roleListToRoleDtoList( dom.getRoles() );
        introductions = introductionListToIntroductionDtoList( dom.getIntroductions() );

        GetScenarioDetailsUseCase.ScenarioDto scenarioDto = new GetScenarioDetailsUseCase.ScenarioDto( id, name, description, contexts, roles, introductions );

        return scenarioDto;
    }

    @Override
    public GetScenarioDetailsUseCase.IntroductionDto toDto(Introduction dom) {
        if ( dom == null ) {
            return null;
        }

        UUID performer = null;
        UUID context = null;
        String performerName = null;
        String contextName = null;
        UUID id = null;
        String spokenText = null;
        Optional<String> thoughtText = null;
        Optional<String> actionText = null;

        performer = domPerformerId( dom );
        context = domContextId( dom );
        performerName = domPerformerName( dom );
        contextName = domContextName( dom );
        id = dom.getId();
        spokenText = dom.getSpokenText();
        thoughtText = dom.getThoughtText();
        actionText = dom.getActionText();

        GetScenarioDetailsUseCase.IntroductionDto introductionDto = new GetScenarioDetailsUseCase.IntroductionDto( id, spokenText, thoughtText, actionText, performer, performerName, context, contextName );

        return introductionDto;
    }

    @Override
    public ScenarioEntity toEntity(Scenario dom) {
        if ( dom == null ) {
            return null;
        }

        ScenarioEntity scenarioEntity = new ScenarioEntity();

        scenarioEntity.setId( dom.getId() );
        scenarioEntity.setName( dom.getName() );
        scenarioEntity.setDescription( dom.getDescription() );

        return scenarioEntity;
    }

    @Override
    public ContextEntity toEntity(Context dom, UUID scenario) {
        if ( dom == null && scenario == null ) {
            return null;
        }

        ContextEntity contextEntity = new ContextEntity();

        if ( dom != null ) {
            contextEntity.setId( dom.getId() );
            contextEntity.setName( dom.getName() );
            contextEntity.setPhysicalDescription( dom.getPhysicalDescription() );
        }
        contextEntity.setScenario( scenario );

        return contextEntity;
    }

    @Override
    public RoleEntity toEntity(Role dom, UUID scenario) {
        if ( dom == null && scenario == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        if ( dom != null ) {
            roleEntity.setId( dom.getId() );
            roleEntity.setName( dom.getName() );
            roleEntity.setDetails( dom.getDetails() );
        }
        roleEntity.setScenario( scenario );

        return roleEntity;
    }

    @Override
    public IntroductionEntity toEntity(Introduction dom, UUID scenario) {
        if ( dom == null && scenario == null ) {
            return null;
        }

        IntroductionEntity introductionEntity = new IntroductionEntity();

        if ( dom != null ) {
            introductionEntity.setPerformer( domPerformerId( dom ) );
            introductionEntity.setContext( domContextId( dom ) );
            introductionEntity.setId( dom.getId() );
            introductionEntity.setSpokenText( dom.getSpokenText() );
            introductionEntity.setThoughtText( map( dom.getThoughtText() ) );
            introductionEntity.setActionText( map( dom.getActionText() ) );
        }
        introductionEntity.setScenario( scenario );

        return introductionEntity;
    }

    protected GetScenarioDetailsUseCase.ContextDto contextToContextDto(Context context) {
        if ( context == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String physicalDescription = null;

        id = context.getId();
        name = context.getName();
        physicalDescription = context.getPhysicalDescription();

        GetScenarioDetailsUseCase.ContextDto contextDto = new GetScenarioDetailsUseCase.ContextDto( id, name, physicalDescription );

        return contextDto;
    }

    protected List<GetScenarioDetailsUseCase.ContextDto> contextListToContextDtoList(List<Context> list) {
        if ( list == null ) {
            return null;
        }

        List<GetScenarioDetailsUseCase.ContextDto> list1 = new ArrayList<GetScenarioDetailsUseCase.ContextDto>( list.size() );
        for ( Context context : list ) {
            list1.add( contextToContextDto( context ) );
        }

        return list1;
    }

    protected GetScenarioDetailsUseCase.RoleDto roleToRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String details = null;

        id = role.getId();
        name = role.getName();
        details = role.getDetails();

        GetScenarioDetailsUseCase.RoleDto roleDto = new GetScenarioDetailsUseCase.RoleDto( id, name, details );

        return roleDto;
    }

    protected List<GetScenarioDetailsUseCase.RoleDto> roleListToRoleDtoList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<GetScenarioDetailsUseCase.RoleDto> list1 = new ArrayList<GetScenarioDetailsUseCase.RoleDto>( list.size() );
        for ( Role role : list ) {
            list1.add( roleToRoleDto( role ) );
        }

        return list1;
    }

    protected List<GetScenarioDetailsUseCase.IntroductionDto> introductionListToIntroductionDtoList(List<Introduction> list) {
        if ( list == null ) {
            return null;
        }

        List<GetScenarioDetailsUseCase.IntroductionDto> list1 = new ArrayList<GetScenarioDetailsUseCase.IntroductionDto>( list.size() );
        for ( Introduction introduction : list ) {
            list1.add( toDto( introduction ) );
        }

        return list1;
    }

    private UUID domPerformerId(Introduction introduction) {
        Role performer = introduction.getPerformer();
        if ( performer == null ) {
            return null;
        }
        return performer.getId();
    }

    private UUID domContextId(Introduction introduction) {
        Context context = introduction.getContext();
        if ( context == null ) {
            return null;
        }
        return context.getId();
    }

    private String domPerformerName(Introduction introduction) {
        Role performer = introduction.getPerformer();
        if ( performer == null ) {
            return null;
        }
        return performer.getName();
    }

    private String domContextName(Introduction introduction) {
        Context context = introduction.getContext();
        if ( context == null ) {
            return null;
        }
        return context.getName();
    }
}
