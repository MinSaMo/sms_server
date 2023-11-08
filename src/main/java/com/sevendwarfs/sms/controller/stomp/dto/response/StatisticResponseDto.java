package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StatisticResponseDto {

  private int month;
  private Map<Integer, WeeklyStatisticResponseDto> weeks;
  private LocalDateTime timestamp;

  @Builder
  public StatisticResponseDto(int month, Map<Integer, WeeklyStatisticResponseDto> weeks) {
    this.month = month;
    this.weeks = weeks;
    timestamp = LocalDateTime.now();
  }

  public static StatisticResponseDto mock() {
    Map<Integer, WeeklyStatisticResponseDto> map = new HashMap<>();
    List<WeeklyStatisticResponseDto> list = WeeklyStatisticResponseDto.mock();
    for (int i = 0; i < list.size(); i++) {
      map.put(i + 1, list.get(i));
    }
    return StatisticResponseDto.builder()
        .month(9)
        .weeks(map)
        .build();
  }

}
