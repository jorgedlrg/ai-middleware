package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface MindRepository extends JpaRepository<MindEntity, UUID>  {}
