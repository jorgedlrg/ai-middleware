package com.jorgedelarosa.aimiddleware.adapter.out.embeddings;

import com.jorgedelarosa.aimiddleware.application.port.out.ExtractRelevantMemoryFragmentsOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class MemoryFragmentAdapter implements ExtractRelevantMemoryFragmentsOutPort{

  @Override
  public List<MemoryFragment> query(Memory memory) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

}
