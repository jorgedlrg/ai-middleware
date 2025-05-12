package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private Scenario scenario;
  private List<Interaction> interactions;

  public Session(Scenario scenario) {
    super(Session.class, UUID.randomUUID());
    this.scenario = scenario;
    this.interactions = new ArrayList<>();
  }
  
  
  public void interact(String text, Role role){
    interactions.add(new Interaction("", text, "", role));
  }

  public Scenario getScenario() {
    return scenario;
  }

  public List<Interaction> getInteractions() {
    return interactions;
  }
}
