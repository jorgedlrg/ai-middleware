package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatRequest;
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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
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
    List<GenericChatMessage> messages = new ArrayList<>();
    messages.add(new GenericChatMessage("user", createContextMessage(cmd)));
    messages.add(new GenericChatMessage("user", createActorsMessage(cmd)));
    messages.add(new GenericChatMessage("user", createRoleMessage(cmd)));

    for (Interaction interaction : cmd.session().getInteractions()) {
      String role = "assistant";
      if (interaction.isUser()) {
        role = "user";
      }
      messages.add(new GenericChatMessage(role, interaction.getSpokenText()));
    }

    String client = "openrouter";

    GenericChatRequest req =
        new GenericChatRequest(
            client.equals("openrouter") ? openRouterClient.MODEL : ollamaClient.MODEL, messages);

    MachineResponse machineResponse;
    switch (client) {
      case "openrouter" -> {
        OpenRouterChatCompletionResponse res =
            openRouterClient.chatCompletion(
                ChatMapper.INSTANCE.toOpenRouterChatCompletionRequest(req));
        machineResponse = new MachineResponse(res.choices().getFirst().message().content());
      }
      case "ollama" -> {
        OllamaChatResponse res =
            ollamaClient.chatCompletion(ChatMapper.INSTANCE.toOllamaChatMessage(req));
        machineResponse = new MachineResponse(res.message().content());
      }
      default -> throw new AssertionError();
    }
    return machineResponse;
  }

  private String createContextMessage(Command cmd) {
    Map<String, Object> contextVars = new HashMap();
    contextVars.put("name", cmd.currentContext().getName());
    contextVars.put("description", cmd.currentContext().getPhysicalDescription());

    return templateEngine.process("context", new Context(Locale.ENGLISH, contextVars));
  }

  private String createActorsMessage(Command cmd) {
    Map<String, Object> actorsVars = new HashMap();
    actorsVars.put("characters", cmd.actors());

    return templateEngine.process("actors", new Context(Locale.ENGLISH, actorsVars));
  }

  private String createRoleMessage(Command cmd) {
    return "You're " + cmd.actor().getName();
  }

  @Mapper
  public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    OllamaChatMessage toOllamaChatMessage(GenericChatMessage a);

    @Mapping(target = "stream", constant = "false")
    OllamaChatRequest toOllamaChatMessage(GenericChatRequest a);

    OpenRouterChatCompletionMessage toOpenRouterChatCompletionMessage(GenericChatMessage a);

    OpenRouterChatCompletionRequest toOpenRouterChatCompletionRequest(GenericChatRequest a);
  }
}
