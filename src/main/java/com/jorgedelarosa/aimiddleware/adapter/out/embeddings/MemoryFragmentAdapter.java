package com.jorgedelarosa.aimiddleware.adapter.out.embeddings;

import com.jorgedelarosa.aimiddleware.application.port.out.ExtractRelevantMemoryFragmentsOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class MemoryFragmentAdapter implements ExtractRelevantMemoryFragmentsOutPort {

  @Override
  public List<MemoryFragment> query(Memory memory, Interaction lastInteraction) {
    InfinispanClient infinispanClient = new InfinispanClient();
    memory.getFragments().stream().forEach(e -> infinispanClient.addTextSegment(e.getText()));
    List<String> matches =
        infinispanClient.query(
            String.format(
                "What's relevant about this? %s %s",
                lastInteraction.getActionText(), lastInteraction.getSpokenText()));
    List<MemoryFragment> result = new ArrayList<>();
    matches.forEach(
        e ->
            result.add(
                memory.getFragments().stream()
                    .filter(m -> m.getText().equals(e))
                    .findFirst()
                    .orElseThrow()));

    return result;
  }
}
