package com.sevendwarfs.sms.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Prompt {

  private String script;
  private Double topP;
  private Double temperature;

  @Builder
  public Prompt(String prompt, Double topP, Double temperature) {
    this.script = prompt;
    this.topP = topP;
    this.temperature = temperature;
  }
}
