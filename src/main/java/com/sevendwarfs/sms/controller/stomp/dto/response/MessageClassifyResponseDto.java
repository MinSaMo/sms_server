package com.sevendwarfs.sms.controller.stomp.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageClassifyResponseDto {

  private Long id;
  private String script;
  private String classification;

  @Builder
  public MessageClassifyResponseDto(Long id, String script, String classification) {
    this.id = id;
    this.script = script;
    this.classification = classification;
  }
}
