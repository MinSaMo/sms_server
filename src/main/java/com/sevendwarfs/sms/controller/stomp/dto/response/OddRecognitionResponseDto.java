package com.sevendwarfs.sms.controller.stomp.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OddRecognitionResponseDto {

  private Long id;

  @Builder
  public OddRecognitionResponseDto(Long id) {
    this.id = id;
  }
}
