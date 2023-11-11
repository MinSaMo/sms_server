package com.sevendwarfs.sms.controller.http.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DialogDetailResponseDto {

  private String oddMessage;
  private List<MessageResponseDto> chats;

  @Builder
  public DialogDetailResponseDto(String oddMessage, List<MessageResponseDto> chats) {
    this.oddMessage = oddMessage;
    this.chats = chats;
  }
}
