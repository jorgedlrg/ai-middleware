package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public interface InteractionRepository extends JpaRepository<InteractionEntity, UUID> {
  List<InteractionEntity> findAllBySession(UUID session);
}
