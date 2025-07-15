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
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MachineInteractionAdapter implements GenerateMachineInteractionOutPort {

  private final OpenRouterClient openRouterClient;
  private final OllamaClient ollamaClient;
  private final TemplateEngine templateEngine;

  @Override
  public MachineResponse execute(Command cmd) {
    String client = "openrouter";

    GenericChatRequest spokenTextReq =
        new GenericChatRequest(
            client.equals("openrouter") ? OpenRouterClient.MODEL_GEMMA_3_27B : ollamaClient.MODEL,
            List.of(createSpeechPromptMessage(cmd)));
    GenericChatRequest moodReq =
        new GenericChatRequest(
            client.equals("openrouter") ? OpenRouterClient.MODEL_GEMMA_3_27B : ollamaClient.MODEL,
            List.of(createMoodPromptMessage(cmd)));

    MachineResponse machineResponse;
    switch (client) {
      case "openrouter" -> {
        OpenRouterChatCompletionResponse res =
            openRouterClient.chatCompletion(
                ChatMapper.INSTANCE.toOpenRouterChatCompletionRequest(spokenTextReq));
        OpenRouterChatCompletionResponse mood =
            openRouterClient.chatCompletion(
                ChatMapper.INSTANCE.toOpenRouterChatCompletionRequest(moodReq));
        machineResponse =
            new MachineResponse(
                res.choices().getFirst().message().content().trim(),
                mood.choices().getFirst().message().content().trim());
      }
      case "ollama" -> {
        OllamaChatResponse res =
            ollamaClient.chatCompletion(ChatMapper.INSTANCE.toOllamaChatMessage(spokenTextReq));
        OllamaChatResponse mood =
            ollamaClient.chatCompletion(ChatMapper.INSTANCE.toOllamaChatMessage(moodReq));
        machineResponse =
            new MachineResponse(res.message().content().trim(), mood.message().content().trim());
      }
      default -> throw new AssertionError();
    }
    return machineResponse;
  }

  private GenericChatMessage createSpeechPromptMessage(Command cmd) {
    return new GenericChatMessage(
        "user",
        templateEngine.process(
            "prompt", new Context(Locale.ENGLISH, createSpeechTemplateVars(cmd))));
  }

  private GenericChatMessage createMoodPromptMessage(Command cmd) {
    return new GenericChatMessage(
        "user",
        templateEngine.process(
            "promptMood", new Context(Locale.ENGLISH, createMoodTemplateVars(cmd))));
  }

  private Map<String, Object> createSpeechTemplateVars(Command cmd) {
    // Replace Performances in Role descriptions
    List<PerformanceDto> performances =
        cmd.performances().stream()
            .map(
                e ->
                    new GenerateMachineInteractionOutPort.PerformanceDto(
                        e.roleName(),
                        e.actorName(),
                        e.physicalDescription(),
                        e.currentOutfit(),
                        e.personality(),
                        replacePerformances(e.roleDescription(), cmd.performances())))
            .toList();

    Map<String, Object> templateVars = new HashMap();
    templateVars.put(
        "scenarioDescription", replacePerformances(cmd.scenarioDescription(), performances));
    templateVars.put("name", cmd.currentContext().getName());
    templateVars.put(
        "description",
        replacePerformances(cmd.currentContext().getPhysicalDescription(), cmd.performances()));
    templateVars.put("actors", performances);
    templateVars.put("previousMessages", cmd.previousMessages());
    templateVars.put("you", cmd.you().getName());
    templateVars.put("language", cmd.replyLanguage());

    return templateVars;
  }

  private Map<String, Object> createMoodTemplateVars(Command cmd) {
    Map<String, Object> templateVars = new HashMap();
    templateVars.put("previousMessages", cmd.previousMessages());
    templateVars.put("you", cmd.you().getName());
    templateVars.put("moods", Mood.values());
    return templateVars;
  }

  /** TODO: Try to do this with Thymeleaf fragments */
  private String replacePerformances(
      String text, List<GenerateMachineInteractionOutPort.PerformanceDto> performances) {
    String replacedText = text;
    for (PerformanceDto performance : performances) {
      replacedText =
          replacedText.replace("{{" + performance.roleName() + "}}", performance.actorName());
    }

    return replacedText;
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
