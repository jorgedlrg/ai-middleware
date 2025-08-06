package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jorgedelarosa.aimiddleware.application.port.mapper.SessionMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetSessionDetailsUseCaseImpl implements GetSessionDetailsUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;

  @Override
  public SessionDto execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();
    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow();

    LoadingCache<UUID, Actor> cache = buildActorCache();

    SessionDto dto =
        new SessionDto(
            session.getId(),
            session.getScenario(),
            session.getCurrentContext(),
            session.getLocale(),
            session.getPerformances().stream()
                .map(
                    e ->
                        SessionMapper.INSTANCE.toDto(
                            cache.getUnchecked(e.getActor()),
                            findRole(scenario, e.getRole()),
                            session.getCurrentInteractions()))
                .toList(),
            session.getCurrentInteractions().stream()
                .map(
                    (e) ->
                        SessionMapper.INSTANCE.toDto(
                            e,
                            cache.getUnchecked(e.getActor()),
                            session.getChildren(e.getParent().orElse(null))))
                .toList());

    return dto;
  }

  private LoadingCache<UUID, Actor> buildActorCache() {
    LoadingCache<UUID, Actor> cache =
        CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                new CacheLoader<UUID, Actor>() {
                  @Override
                  public Actor load(UUID key) {
                    return getActorByIdOutPort.query(key).orElseThrow();
                  }
                });

    return cache;
  }

  private Role findRole(Scenario scenario, UUID role) {
    return scenario.getRoles().stream()
        .filter(e -> e.getId().equals(role))
        .findFirst()
        .orElseThrow();
  }
}
