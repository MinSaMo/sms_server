package com.sevendwarfs.sms.controller.http.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BehaviorDetailResponseDto {

  private Long behaviorId;
  private String  caption;
  private Long videoId;

  @Builder
  public BehaviorDetailResponseDto(Long behaviorId, String caption, Long videoId) {
    this.behaviorId = behaviorId;
    this.caption = caption;
    this.videoId = videoId;
  }
}
