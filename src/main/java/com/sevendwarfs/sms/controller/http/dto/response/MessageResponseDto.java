package com.sevendwarfs.sms.controller.http.dto.response;

import com.sevendwarfs.sms.domain.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {

  private boolean isOdd;
  private String script;
  private String sender;

  @Builder
  public MessageResponseDto(String script, String sender, boolean isOdd) {
    this.script = script;
    this.sender = sender;
    this.isOdd = isOdd;
  }

  public static MessageResponseDto chat(Message message) {
    return MessageResponseDto.builder()
        .script(message.getContent())
        .sender(message.getSender())
        .isOdd(false)
        .build();
  }

  public static MessageResponseDto odd(Message message) {
    return MessageResponseDto.builder()
        .script(message.getContent())
        .sender(message.getSender())
        .isOdd(true)
        .build();
  }


}
