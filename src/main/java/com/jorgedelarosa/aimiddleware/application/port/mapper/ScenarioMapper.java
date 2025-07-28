package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ContextEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.RoleEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ScenarioEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenarioDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.scenario.GetScenariosUseCase;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface ScenarioMapper {
  ScenarioMapper INSTANCE = Mappers.getMapper(ScenarioMapper.class);

  GetScenarioDetailsUseCase.ScenarioDto toDetailsDto(Scenario dom);

  default GetScenariosUseCase.ScenarioDto toDto(Scenario dom) {
    return new GetScenariosUseCase.ScenarioDto(
        dom.getId(), dom.getName(), dom.getContexts().size(), dom.getRoles().size());
  }

  ScenarioEntity toEntity(Scenario dom);

  default List<ContextEntity> toContextEntity(List<Context> dom, UUID scenario) {
    return dom.stream().map(e -> toEntity(e, scenario)).toList();
  }

  default List<RoleEntity> toRoleEntity(List<Role> dom, UUID scenario) {
    return dom.stream().map(e -> toEntity(e, scenario)).toList();
  }

  ContextEntity toEntity(Context dom, UUID scenario);

  RoleEntity toEntity(Role dom, UUID scenario);

  default Context toDom(ContextEntity entity) {
    return Context.restore(entity.getId(), entity.getName(), entity.getPhysicalDescription());
  }

  default Role toDom(RoleEntity entity) {
    return Role.restore(entity.getId(), entity.getName(), entity.getDetails());
  }
}
