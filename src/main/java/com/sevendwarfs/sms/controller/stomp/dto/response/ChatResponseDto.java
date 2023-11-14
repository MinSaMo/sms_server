package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {

  private Long id;
  private String sender;
  private String script;
  private boolean isOdd;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  public static final String USER = "user";
  public static final String ASSISTANT = "assistant";

}
