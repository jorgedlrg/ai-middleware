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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class MachineInteractionAdapter implements GenerateMachineInteractionOutPort {

  private final OpenRouterClient openRouterClient;
  private final OllamaClient ollamaClient;
  private final TemplateEngine templateEngine;

  @Override
  public MachineResponse execute(Command cmd) {

    // FIXME non-final version. I'm experimenting here with the LLM providers.

    MachineResponse machineResponse;

    //         TODO: Add All the scenario in an initial 'user' message.

    Map<String, Object> contextVars = new HashMap();
    contextVars.put("name", cmd.currentContext().getName());
    contextVars.put("description", cmd.currentContext().getPhysicalDescription());
    String contextMessage =
        templateEngine.process("context", new Context(Locale.ENGLISH, contextVars));

    Map<String, Object> actorsVars = new HashMap();
    actorsVars.put("characters", cmd.actors());
    String actorsMessage =
        templateEngine.process("actors", new Context(Locale.ENGLISH, actorsVars));

    String client = "openrouter";
    // FIXME don't use this crappy switch
    switch (client) {
      case "openrouter" -> {
        //FIXME: I should use generic messages and map them in the different clients
        List<OpenRouterChatCompletionMessage> messages = new ArrayList<>();
        messages.add(new OpenRouterChatCompletionMessage("user", contextMessage));
        messages.add(new OpenRouterChatCompletionMessage("user", actorsMessage));
        // FIXME
        messages.add(
            new OpenRouterChatCompletionMessage("user", "You're " + cmd.actor().getName()));

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
        //FIXME: I should use generic messages and map them in the different clients
        List<OllamaChatMessage> messages = new ArrayList<>();
        messages.add(new OllamaChatMessage("assistant", contextMessage));
        messages.add(new OllamaChatMessage("user", actorsMessage));
        // FIXME
        messages.add(new OllamaChatMessage("user", "You're " + cmd.actor().getName()));

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
