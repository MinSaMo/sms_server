package com.sevendwarfs.sms.controller.http.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChartResponseDto {

  private List<ChartItemResponseDto> logs;

  public static ChartResponseDto mock() {
    ChartResponseDto res = new ChartResponseDto();
    res.logs = new ArrayList<>();
    res.logs.add(ChartItemResponseDto.mock("질문", "환자 약 복용 완료"));
    res.logs.add(ChartItemResponseDto.mock("이상발화", "언어탈선 증상"));
    res.logs.add(ChartItemResponseDto.mock("이상발화", "환각 언어탈선 와해된 언어 증상"));
    res.logs.add(ChartItemResponseDto.mock("이상행동", "기이행동 증상"));
    return res;
  }

  @Builder
  public ChartResponseDto(List<ChartItemResponseDto> logs) {
    this.logs = logs;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ChartResponseDto target)) {
      return false;
    }

    if (target.logs.size() != this.logs.size()) {
      return false;
    }

    for (int i = 0; i < this.logs.size(); i++) {
      ChartItemResponseDto targetItem = target.logs.get(i);
      ChartItemResponseDto thisItem = this.logs.get(i);
      if (!targetItem.equals(thisItem)) {
        return false;
      }
    }
    return true;
  }
}
