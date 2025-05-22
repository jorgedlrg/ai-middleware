package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class SessionAdapter implements GetSessionByIdOutPort, SaveSessionOutPort{

  @Override
  public Session query(UUID id) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void save(Session session) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
