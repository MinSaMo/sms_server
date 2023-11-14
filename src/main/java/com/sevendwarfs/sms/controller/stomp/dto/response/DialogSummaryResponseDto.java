package com.sevendwarfs.sms.controller.stomp.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DialogSummaryResponseDto {

  private String summary;

  @Builder
  public DialogSummaryResponseDto(String summary) {
    this.summary = summary;
  }
}
