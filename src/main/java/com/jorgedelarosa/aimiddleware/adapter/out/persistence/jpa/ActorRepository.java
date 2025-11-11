package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface ActorRepository extends JpaRepository<ActorEntity, UUID> {

  List<ActorEntity> findAllByCurrentOutfit(UUID currentOutfit);
}
