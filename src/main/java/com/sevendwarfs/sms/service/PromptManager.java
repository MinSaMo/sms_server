package com.sevendwarfs.sms.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PromptManager {

  private final String classifyMessage;
  private final String global;
  private final String daily;
  private final String recognitionBehavior;
  private final String recognitionMessage;
  private final String summarizeDialog;
  private final String nurseInterview;

  @Autowired
  public PromptManager(
      @Value("${prompt.global}") String global,
      @Value("${prompt.message.classify}") String messageClassify,
      @Value("${prompt.message.daily}") String daily,
      @Value("${prompt.behavior_recognition}") String recognitionBehavior,
      @Value("${prompt.message.recognition}") String recognitionMessage,
      @Value("${prompt.dialog_summarize}") String summarizeDialog,
      @Value("${prompt.nurse_interview}") String nurseInterview) {
    this.global = global;
    this.classifyMessage = messageClassify;
    this.daily = daily;
    this.recognitionBehavior = recognitionBehavior;
    this.recognitionMessage = recognitionMessage;
    this.summarizeDialog = summarizeDialog;
    this.nurseInterview = nurseInterview;
  }
}
