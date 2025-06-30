package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
  List<RoleEntity> findAllByScenario(UUID scenario);

  void deleteAllByScenario(UUID scenario);
}
