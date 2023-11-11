package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BehaviorResponseDto {

  private String caption;
  private boolean isOdd;
  private LocalDateTime timestamp;

  @Builder
  public BehaviorResponseDto(String caption, boolean isOdd) {
    this.caption = caption;
    this.isOdd = isOdd;
    timestamp = LocalDateTime.now();
  }

  public static BehaviorResponseDto mock(String caption) {
    return BehaviorResponseDto.builder()
        .caption(caption)
        .isOdd(Math.random() > 0.5)
        .build();
  }
}
