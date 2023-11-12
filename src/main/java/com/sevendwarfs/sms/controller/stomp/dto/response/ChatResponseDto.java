package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

  private Long id;
  private String sender;
  private String script;
  private LocalDateTime timestamp;

  public static final String USER = "user";
  public static final String ASSISTANT = "assistant";

  @Builder
  public ChatResponseDto(Long id, String script, String sender) {
    this.id = id;
    this.script = script;
    this.sender = sender;
    timestamp = LocalDateTime.now();
  }

}
