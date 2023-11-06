package com.sevendwarfs.sms.controller.http.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChartItemResponseDto {

  private String type;
  private String content;
  private LocalDateTime timestamp;

  @Builder
  public ChartItemResponseDto(String type, String content, LocalDateTime timestamp) {
    this.type = type;
    this.content = content;
    this.timestamp = timestamp;
  }

  public static ChartItemResponseDto mock(String type, String content) {
    return ChartItemResponseDto.builder()
        .type(type)
        .content(content)
        .timestamp(LocalDateTime.now())
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ChartItemResponseDto target)) {
      return false;
    }
    // Compare content and type
    boolean contentEquals = this.content.equals(target.content);
    boolean typeEquals = this.type.equals(target.type);

    // Compare timestamp up to minutes
    boolean timestampEquals = this.timestamp.getYear() == target.timestamp.getYear()
        && this.timestamp.getMonth() == target.timestamp.getMonth()
        && this.timestamp.getDayOfMonth() == target.timestamp.getDayOfMonth()
        && this.timestamp.getHour() == target.timestamp.getHour()
        && this.timestamp.getMinute() == target.timestamp.getMinute();

    return contentEquals && typeEquals && timestampEquals;
  }
}
