package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BehaviorResponseDto {

  private String caption;
  private boolean isOdd;
  private LocalDateTime timestamp;

  @Builder
  public BehaviorResponseDto(String caption, boolean isOdd, LocalDateTime timestamp) {
    this.caption = caption;
    this.isOdd = isOdd;
    this.timestamp = timestamp;
  }

  public static BehaviorResponseDto mock(String caption) {
    return BehaviorResponseDto.builder()
        .caption(caption)
        .isOdd(Math.random() > 0.5)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
