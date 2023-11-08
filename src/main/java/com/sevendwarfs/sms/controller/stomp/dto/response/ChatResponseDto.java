package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

  private String script;
  private LocalDateTime timestamp;

  @Builder
  public ChatResponseDto(String script) {
    this.script = script;
    timestamp = LocalDateTime.now();
  }

  public static ChatResponseDto mock(String script) {
    String reply = String.format("user say : %s", script);
    return ChatResponseDto.builder()
        .script(reply)
        .build();
  }
}
