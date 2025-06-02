package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class MachineInteractionAdapter implements GenerateMachineInteractionOutPort {

  private final OpenRouterClient openRouterClient;
  private final OllamaClient ollamaClient;

  @Override
  public MachineResponse execute(Command cmd) {

    MachineResponse machineResponse;

    String client = "openrouter"; // FIXME
    switch (client) {
      case "openrouter" -> {
        List<OpenRouterChatCompletionMessage> messages = new ArrayList<>();
        for (Interaction interaction : cmd.session().getInteractions()) {
          String role = "assistant";
          if (interaction.isUser()) {
            role = "user";
          }
          OpenRouterChatCompletionMessage ocm =
              new OpenRouterChatCompletionMessage(role, interaction.getSpokenText());
          messages.add(ocm);
        }

        OpenRouterChatCompletionRequest req =
            new OpenRouterChatCompletionRequest(openRouterClient.MODEL, messages);
        OpenRouterChatCompletionResponse res = openRouterClient.chatCompletion(req);
        machineResponse = new MachineResponse(res.choices().getFirst().message().content());
      }
      case "ollama" -> {
        machineResponse = new MachineResponse("TODO");
      }
      default -> throw new AssertionError();
    }
    return machineResponse;
  }
}
