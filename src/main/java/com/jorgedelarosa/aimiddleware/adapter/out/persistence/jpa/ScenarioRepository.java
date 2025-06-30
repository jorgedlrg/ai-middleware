package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public interface ScenarioRepository extends JpaRepository<ScenarioEntity, UUID> {

}
