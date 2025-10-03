package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface MessageMapper {
  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  default GenerateMachineInteractionOutPort.PreviousMessage toMessage(
      String actorName, Interaction interaction) {
    return new GenerateMachineInteractionOutPort.PreviousMessage(
        actorName,
        interaction.getActionText().map(e -> e.getText()),
        interaction.getSpokenText().getText());
  }

  default GenerateMachineInteractionOutPort.PerformanceDto toDto(
      Performance performance,
      Scenario scenario,
      List<Actor> featuredActors,
      List<Outfit> wornOutfits) {
    Role role =
        scenario.getRoles().stream()
            .filter(r -> r.getId().equals(performance.getRole()))
            .findFirst()
            .orElseThrow();
    Actor actor =
        featuredActors.stream()
            .filter(a -> a.getId().equals(performance.getActor()))
            .findFirst()
            .orElseThrow();
    String outfit = null;
    if (actor.getCurrentOutfit().isPresent()) {
      outfit =
          wornOutfits.stream()
              .filter(o -> o.getId().equals(actor.getCurrentOutfit().get()))
              .findFirst()
              .orElseThrow()
              .getDescription();
    }

    return new GenerateMachineInteractionOutPort.PerformanceDto(
        role.getName(),
        actor.getName(),
        actor.getProfile(),
        actor.getPhysicalDescription(),
        Optional.ofNullable(outfit),
        actor.getMind().map(e -> e.getPersonality()),
        role.getDetails());
  }
}
