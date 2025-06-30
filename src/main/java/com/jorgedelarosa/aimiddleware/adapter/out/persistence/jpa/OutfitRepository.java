package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface OutfitRepository extends JpaRepository<OutfitEntity, UUID> {
  List<OutfitEntity> findAllByActor(UUID actor);
}
