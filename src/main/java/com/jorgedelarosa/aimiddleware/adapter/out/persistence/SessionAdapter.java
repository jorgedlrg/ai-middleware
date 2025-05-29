package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class SessionAdapter implements GetSessionByIdOutPort, SaveSessionOutPort{

  @Override
  public Optional<Session> query(UUID id) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void save(Session session) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
