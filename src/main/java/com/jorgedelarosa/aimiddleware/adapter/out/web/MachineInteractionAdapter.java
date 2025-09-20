package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.GenericChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OllamaChatResponse;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionMessage;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionRequest;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterChatCompletionResponse;
import com.jorgedelarosa.aimiddleware.adapter.out.web.dto.OpenRouterReasoning;
import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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

  private final TemplateEngine templateEngine;

  @Override
  public MachineResponse execute(Command cmd) {
    String[] thoughts = new String[2];
    String[] action = new String[2];
    String mood = null;
    if (cmd.settings().thoughtsEnabled()) {
      thoughts =
          doInference(
              new GenericChatMessage(
                  "user",
                  templateEngine.process(
                      "promptThoughts",
                      new Context(Locale.ENGLISH, createThoughtsTemplateVars(cmd))),
                  ""),
              cmd.settings(),
              cmd.settings().thoughtsReasoning());
    }
    if (cmd.settings().actionsEnabled()) {
      action =
          doInference(
              new GenericChatMessage(
                  "user",
                  templateEngine.process(
                      "promptAction",
                      new Context(Locale.ENGLISH, createActionTemplateVars(cmd, thoughts[0]))),
                  ""),
              cmd.settings(),
              cmd.settings().actionsReasoning());
    }
    String[] speech =
        doInference(
            new GenericChatMessage(
                "user",
                templateEngine.process(
                    "promptSpeech",
                    new Context(
                        Locale.ENGLISH, createSpeechTemplateVars(cmd, thoughts[0], action[0]))),
                ""),
            cmd.settings(),
            cmd.settings().speechReasoning());
    if (cmd.settings().moodEnabled()) {
      mood =
          doInference(
              new GenericChatMessage(
                  "user",
                  templateEngine.process(
                      "promptMood",
                      new Context(
                          Locale.ENGLISH, createMoodTemplateVars(cmd, thoughts[0], speech[0]))),
                  ""),
              cmd.settings(),
              false)[0];
    }
    return new MachineResponse(
        thoughts[0] != null
            ? Optional.of(new TextDto(thoughts[0], Optional.ofNullable(thoughts[1])))
            : Optional.empty(),
        action[0] != null
            ? Optional.of(new TextDto(action[0], Optional.ofNullable(action[1])))
            : Optional.empty(),
        new TextDto(speech[0], Optional.ofNullable(speech[1])),
        mood);
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
    templateVars.put("memoryFragments", cmd.memoryFragments());

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

  /**
   * @param msg
   * @param settings
   * @param reasoning
   * @return [0] text, [1] reasoning
   */
  private String[] doInference(
      GenericChatMessage msg,
      GenerateMachineInteractionOutPort.TextGenSettingsDto settings,
      boolean reasoning) {
    String[] text = new String[2];
    text[1] = null;

    switch (settings.textgenProvider()) {
      case "openrouter" -> {
        GenericChatRequest req =
            new GenericChatRequest(settings.openrouterModel(), List.of(msg), reasoning);
        OpenRouterClient client = new OpenRouterClient(settings.openrouterApikey());
        OpenRouterChatCompletionResponse response =
            client.chatCompletion(ChatMapper.INSTANCE.toOpenRouterChatCompletionRequest(req));
        text[0] = response.choices().getFirst().message().content();
        if (reasoning) {
          text[1] = response.choices().getFirst().message().reasoning();
        }
      }
      case "ollama" -> {
        GenericChatRequest req =
            new GenericChatRequest(settings.ollamaModel(), List.of(msg), reasoning);
        OllamaClient client = new OllamaClient(settings.ollamaHost());
        OllamaChatResponse response =
            client.chatCompletion(ChatMapper.INSTANCE.toOllamaChatMessage(req));
        text[0] = response.message().content();
        if (reasoning) {
          text[1] = response.message().thinking();
        }
      }
      default -> throw new AssertionError();
    }

    return text;
  }

  @Mapper
  public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target = "thinking", source = "reasoning")
    OllamaChatMessage toOllamaChatMessage(GenericChatMessage a);

    @Mapping(target = "stream", constant = "false")
    @Mapping(target = "think", source = "reasoning")
    OllamaChatRequest toOllamaChatMessage(GenericChatRequest a);

    OpenRouterChatCompletionMessage toOpenRouterChatCompletionMessage(GenericChatMessage a);

    default OpenRouterChatCompletionRequest toOpenRouterChatCompletionRequest(
        GenericChatRequest a) {
      return new OpenRouterChatCompletionRequest(
          a.model(),
          a.messages().stream().map(e -> toOpenRouterChatCompletionMessage(e)).toList(),
          new OpenRouterReasoning(a.reasoning()));
    }
  }
}
