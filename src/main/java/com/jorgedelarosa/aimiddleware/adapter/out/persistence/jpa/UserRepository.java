package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jorge
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {}