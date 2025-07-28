package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface IntroductionRepository extends JpaRepository<IntroductionEntity, UUID> {
  List<IntroductionEntity> findAllByScenario(UUID scenario);

  void deleteAllByScenario(UUID scenario);
}
