package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface PerformanceRepository
    extends JpaRepository<PerformanceEntity, PerformanceId> {

  List<PerformanceEntity> findAllByPerformanceIdSession(UUID session);
}
