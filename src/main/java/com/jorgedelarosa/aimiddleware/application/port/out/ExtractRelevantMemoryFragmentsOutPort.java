package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import java.util.List;

/**
 * @author jorge
 */
public interface ExtractRelevantMemoryFragmentsOutPort {

  List<MemoryFragment> query(Memory memory);
}
