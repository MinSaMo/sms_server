package com.sevendwarfs.sms.controller.stomp.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PromptResponseDto {

  private String script;
  private String prompt;

  @Builder
  public PromptResponseDto(String script, String prompt) {
    this.script = script;
    this.prompt = prompt;
  }
}
