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
    String client = "openrouter";

    List<GenericChatMessage> messages = new ArrayList<>();
    messages.add(createPromptMessage(cmd));
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

  private GenericChatMessage createPromptMessage(Command cmd) {
    Map<String, Object> templateVars = new HashMap();
    templateVars.put("name", cmd.currentContext().getName());
    templateVars.put("description", cmd.currentContext().getPhysicalDescription());
    templateVars.put("actors", cmd.actors());
    templateVars.put("previousMessages", cmd.previousMessages());
    templateVars.put("you", cmd.you().getName());
    templateVars.put("language", cmd.replyLanguage());

    return new GenericChatMessage(
        "user", templateEngine.process("prompt", new Context(Locale.ENGLISH, templateVars)));
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
