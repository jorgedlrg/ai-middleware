package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author jorge
 */
public interface GetUserByIdOutPort {
  
  public Optional<User> query(UUID id);

}
