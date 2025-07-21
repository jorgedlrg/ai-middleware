package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "settings")
@Data
public class SettingsEntity {
  @Id private UUID userid;

  @Column(name = "textgen_provider")
  private String textgenProvider;

  @Column(name = "openrouter_apikey")
  private String openrouterApikey;

  @Column(name = "openrouter_model")
  private String openrouterModel;

  @Column(name = "ollama_host")
  private String ollamaHost;

  @Column(name = "ollama_model")
  private String ollamaModel;

  @Column(name = "actions_enabled")
  private boolean actionsEnabled;

  @Column(name = "mood_enabled")
  private boolean moodEnabled;

  @Column(name = "thoughts_enabled")
  private boolean thoughtsEnabled;
}
