package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ContextRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.IntroductionRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.RoleRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ScenarioEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ScenarioRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.ScenarioMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteScenarioOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenariosOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Introduction;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
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
public class ScenarioAdapter
    implements GetScenarioByIdOutPort,
        GetScenariosOutPort,
        SaveScenarioOutPort,
        DeleteScenarioOutPort {

  private final ScenarioRepository scenarioRepository;
  private final ContextRepository contextRepository;
  private final RoleRepository roleRepository;
  private final IntroductionRepository introductionRepository;

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
    List<Introduction> introductions =
        introductionRepository.findAllByScenario(se.getId()).stream()
            .map(
                (e) ->
                    ScenarioMapper.INSTANCE.toDom(
                        e,
                        roles.stream()
                            .filter(r -> r.getId().equals(e.getPerformer()))
                            .findFirst()
                            .orElseThrow(),
                        contexts.stream()
                            .filter(c -> c.getId().equals(e.getContext()))
                            .findFirst()
                            .orElseThrow()))
            .toList();
    return Scenario.restore(
        se.getId(), se.getName(), se.getDescription(), contexts, roles, introductions);
  }

  @Override
  public void save(Scenario scenario) {
    contextRepository.deleteAllByScenario(scenario.getId());
    roleRepository.deleteAllByScenario(scenario.getId());
    introductionRepository.deleteAllByScenario(scenario.getId());
    scenarioRepository.save(ScenarioMapper.INSTANCE.toEntity(scenario));
    contextRepository.saveAll(
        ScenarioMapper.INSTANCE.toContextEntity(scenario.getContexts(), scenario.getId()));
    roleRepository.saveAll(
        ScenarioMapper.INSTANCE.toRoleEntity(scenario.getRoles(), scenario.getId()));
    introductionRepository.saveAll(
        ScenarioMapper.INSTANCE.toIntroEntity(scenario.getIntroductions(), scenario.getId()));
  }

  @Override
  public void delete(Scenario scenario) {
    contextRepository.deleteAllByScenario(scenario.getId());
    roleRepository.deleteAllByScenario(scenario.getId());
    scenarioRepository.deleteById(scenario.getId());
  }
}
