package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

  private Long id;
  private String script;
  private LocalDateTime timestamp;

  @Builder
  public ChatResponseDto(Long id, String script) {
    this.id = id;
    this.script = script;
    timestamp = LocalDateTime.now();
  }

  public static ChatResponseDto mock(String script) {
    String reply = String.format("user say : %s", script);
    return ChatResponseDto.builder()
        .id(0L)
        .script(reply)
        .build();
  }

}
