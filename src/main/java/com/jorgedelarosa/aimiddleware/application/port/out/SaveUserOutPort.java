package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.user.User;

/**
 * @author jorge
 */
public interface SaveUserOutPort {
  public void save(User user);
}
