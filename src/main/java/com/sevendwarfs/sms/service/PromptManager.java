package com.sevendwarfs.sms.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PromptManager {

  private final String messageClassify;
  private final String global;
  private final String daily;
  private final String recognitionBehavior;
  private final String summarizeDialog;

  @Autowired
  public PromptManager(
      @Value("${prompt.global}") String global,
      @Value("${prompt.message.classify}") String messageClassify,
      @Value("${prompt.message.daily}") String daily,
      @Value("${prompt.behavior_recognition}") String recognitionBehavior,
      @Value("${prompt.dialog_summarize}") String summarizeDialog) {
    this.global = global;
    this.messageClassify = messageClassify;
    this.daily = daily;
    this.recognitionBehavior = recognitionBehavior;
    this.summarizeDialog = summarizeDialog;
  }
}
