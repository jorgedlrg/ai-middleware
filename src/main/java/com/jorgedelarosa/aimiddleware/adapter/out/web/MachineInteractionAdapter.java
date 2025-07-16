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
  private final String TEXT_GEN_CLIENT = "openrouter";

  @Override
  public MachineResponse execute(Command cmd) {
    String thoughts =
        doInference(
            new GenericChatMessage(
                "user",
                templateEngine.process(
                    "promptThoughts",
                    new Context(Locale.ENGLISH, createThoughtsTemplateVars(cmd)))));
    String action =
        doInference(
            new GenericChatMessage(
                "user",
                templateEngine.process(
                    "promptAction",
                    new Context(Locale.ENGLISH, createActionTemplateVars(cmd, thoughts)))));
    String speech =
        doInference(
            new GenericChatMessage(
                "user",
                templateEngine.process(
                    "promptSpeech",
                    new Context(Locale.ENGLISH, createSpeechTemplateVars(cmd, thoughts, action)))));
    String mood =
        doInference(
            new GenericChatMessage(
                "user",
                templateEngine.process(
                    "promptMood",
                    new Context(Locale.ENGLISH, createMoodTemplateVars(cmd, thoughts, speech)))));

    return new MachineResponse(thoughts, action, speech, mood);
  }

  private Map<String, Object> createThoughtsTemplateVars(Command cmd) {
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

  private Map<String, Object> createActionTemplateVars(Command cmd, String currentThoughts) {
    Map<String, Object> templateVars = createThoughtsTemplateVars(cmd);
    templateVars.put("currentThoughts", currentThoughts);
    return templateVars;
  }

  private Map<String, Object> createSpeechTemplateVars(
      Command cmd, String currentThoughts, String currentAction) {
    Map<String, Object> templateVars = createThoughtsTemplateVars(cmd);
    templateVars.put("currentThoughts", currentThoughts);
    templateVars.put("currentAction", currentAction);
    return templateVars;
  }

  private Map<String, Object> createMoodTemplateVars(
      Command cmd, String currentThoughts, String lastSpeech) {
    Map<String, Object> templateVars = new HashMap();
    templateVars.put("previousMessages", cmd.previousMessages());
    templateVars.put("you", cmd.you().getName());
    templateVars.put("currentThoughts", currentThoughts);
    templateVars.put("lastSpeech", lastSpeech);
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

  private String doInference(GenericChatMessage msg) {
    String text;
    switch (TEXT_GEN_CLIENT) {
      case "openrouter" -> {
        GenericChatRequest req =
            new GenericChatRequest(OpenRouterClient.MODEL_GEMMA_3_12B, List.of(msg));
        OpenRouterChatCompletionResponse response =
            openRouterClient.chatCompletion(
                ChatMapper.INSTANCE.toOpenRouterChatCompletionRequest(req));
        text = response.choices().getFirst().message().content();
      }
      case "ollama" -> {
        GenericChatRequest req = new GenericChatRequest(OllamaClient.GEMMA3_12B, List.of(msg));
        OllamaChatResponse response =
            ollamaClient.chatCompletion(ChatMapper.INSTANCE.toOllamaChatMessage(req));
        text = response.message().content();
      }
      default -> throw new AssertionError();
    }

    return text.trim();
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
