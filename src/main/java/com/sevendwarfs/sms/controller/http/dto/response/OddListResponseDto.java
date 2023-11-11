package com.sevendwarfs.sms.controller.http.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OddListResponseDto {

  private List<OddItemResponseDto> logs;

  @Builder
  public OddListResponseDto(List<OddItemResponseDto> logs) {
    this.logs = logs;
  }

  public static OddListResponseDto mock() {
    OddListResponseDto res = new OddListResponseDto();
    res.logs = new ArrayList<>();

    for (long i = 0; i < 7; i++) {
      res.logs.add(OddItemResponseDto.builder()
          .id(i)
          .type("이상발화")
          .timestamp(LocalDateTime.now())
          .content("저기 창문너머에 빨간옷을 입은 여자가 나에게 손을 흔들고 있어요.")
          .reason("발화가 실제로 존재하지 않는 시각적 환영을 묘사하는 환각의 가능성과 발화가 통상적이지 않은 대화의 방식으로 탈선의 증상을 나타냅니다.")
          .reference(1L)
          .build());
    }
    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof OddListResponseDto target)) {
      return false;
    }

    if (target.logs.size() != this.logs.size()) {
      return false;
    }

    for (int i = 0; i < this.logs.size(); i++) {
      OddItemResponseDto targetItem = target.logs.get(i);
      OddItemResponseDto thisItem = this.logs.get(i);
      if (!targetItem.equals(thisItem)) {
        return false;
      }
    }
    return true;
  }
}
