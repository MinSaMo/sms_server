package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorResponseDto {

  private String caption;
  private boolean isOdd;
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  public static BehaviorResponseDto mock(String caption) {
    return BehaviorResponseDto.builder()
        .caption(caption)
        .isOdd(Math.random() > 0.5)
        .build();
  }
}
