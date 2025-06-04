package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatResponse;
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

    // FIXME non-final version. I'm experimenting here with the LLM providers.

    MachineResponse machineResponse;

    String client = "openrouter";
    // FIXME don't use this crappy switch
    switch (client) {
      case "openrouter" -> {
        List<OpenRouterChatCompletionMessage> messages = new ArrayList<>();
        //         TODO: Add All the scenario in an initial 'user' message.
        //         Maybe use thymeleaf for templating?
        messages.add(
            new OpenRouterChatCompletionMessage(
                "user",
                new StringBuilder()
                    .append("This interaction happens in this context: {")
                    .append(cmd.currentContext().getPhysicalDescription())
                    .append("}")
                    .toString()));

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
        List<OllamaChatMessage> messages = new ArrayList<>();
        for (Interaction interaction : cmd.session().getInteractions()) {
          String role = "assistant";
          if (interaction.isUser()) {
            role = "user";
          }
          OllamaChatMessage ocm = new OllamaChatMessage(role, interaction.getSpokenText());
          messages.add(ocm);
        }

        OllamaChatRequest req = new OllamaChatRequest(ollamaClient.MODEL, messages, false);
        OllamaChatResponse res = ollamaClient.chatCompletion(req);
        machineResponse = new MachineResponse(res.message().content());
      }
      default -> throw new AssertionError();
    }
    return machineResponse;
  }
}
