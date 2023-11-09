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

  @Autowired
  public PromptManager(
      @Value("${prompt.global}") String global,
      @Value("${prompt.message.classify}") String messageClassify,
      @Value("${prompt.message.daily}") String daily) {
    this.global = global;
    this.messageClassify = messageClassify;
    this.daily = daily;
  }
}
