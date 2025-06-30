package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenariosOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class ScenarioAdapter
    implements GetScenarioByIdOutPort, GetScenariosOutPort, SaveScenarioOutPort {

  private final ScenarioRepository scenarioRepository;
  private final ContextRepository contextRepository;
  private final RoleRepository roleRepository;

  @Override
  public Optional<Scenario> query(UUID id) {
    return scenarioRepository.findById(id).map(e -> restoreScenario(e));
  }

  @Override
  public List<Scenario> query() {
    return scenarioRepository.findAll().stream().map(e -> restoreScenario(e)).toList();
  }

  private Scenario restoreScenario(ScenarioEntity se) {
    List<Context> contexts =
        contextRepository.findAllByScenario(se.getId()).stream()
            .map((e) -> ScenarioMapper.INSTANCE.toDom(e))
            .toList();
    List<Role> roles =
        roleRepository.findAllByScenario(se.getId()).stream()
            .map((e) -> ScenarioMapper.INSTANCE.toDom(e))
            .toList();
    return Scenario.restore(se.getId(), se.getName(), contexts, roles);
  }

  @Override
  public void save(Scenario scenario) {
    contextRepository.deleteAllByScenario(scenario.getId());
    roleRepository.deleteAllByScenario(scenario.getId());
    scenarioRepository.save(ScenarioMapper.INSTANCE.toEntity(scenario));
    contextRepository.saveAll(
        ScenarioMapper.INSTANCE.toContextEntity(scenario.getContexts(), scenario.getId()));
    roleRepository.saveAll(
        ScenarioMapper.INSTANCE.toRoleEntity(scenario.getRoles(), scenario.getId()));
  }

  @Mapper
  public interface ScenarioMapper {
    ScenarioMapper INSTANCE = Mappers.getMapper(ScenarioMapper.class);

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
}
