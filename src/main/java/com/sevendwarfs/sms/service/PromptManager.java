package com.sevendwarfs.sms.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PromptManager {

  private final Prompt classifyMessage;
  private final Prompt global;
  private final Prompt daily;
  private final Prompt recognitionBehavior;
  private final Prompt recognitionMessage;
  private final Prompt summarizeDialog;
  private final Prompt nurseInterview;

  @Autowired
  public PromptManager(
      @Value("${prompt.global}") String global,
      @Value("${prompt.message.classify}") String messageClassify,
      @Value("${prompt.message.daily}") String daily,
      @Value("${prompt.behavior_recognition}") String recognitionBehavior,
      @Value("${prompt.message.recognition}") String recognitionMessage,
      @Value("${prompt.dialog_summarize}") String summarizeDialog,
      @Value("${prompt.nurse_interview}") String nurseInterview) {
    this.global = Prompt.builder()
        .prompt(global)
        .topP(null)
        .temperature(null)
        .build();
    this.classifyMessage = Prompt.builder()
        .prompt(messageClassify)
        .topP(0.1)
        .temperature(0.2)
        .build();
    this.daily = Prompt.builder()
        .prompt(daily)
        .topP(0.3)
        .temperature(0.4)
        .build();
    this.recognitionBehavior = Prompt.builder()
        .prompt(recognitionBehavior)
        .topP(0.5)
        .temperature(0.3)
        .build();
    this.recognitionMessage = Prompt.builder()
        .prompt(recognitionMessage)
        .topP(0.5)
        .temperature(0.3)
        .build();
    this.summarizeDialog = Prompt.builder()
        .prompt(summarizeDialog)
        .topP(0.5)
        .temperature(0.3)
        .build();
    this.nurseInterview = Prompt.builder()
        .prompt(nurseInterview)
        .topP(0.5)
        .temperature(0.3)
        .build();
  }
}
