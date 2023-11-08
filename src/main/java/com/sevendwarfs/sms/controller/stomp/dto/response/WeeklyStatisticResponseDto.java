package com.sevendwarfs.sms.controller.stomp.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WeeklyStatisticResponseDto {

  private int keeyee;
  private int hwangak;
  private int mangsang;
  private int wahae;
  private int talsun;

  @Builder
  public WeeklyStatisticResponseDto(int keeyee, int hwangak, int mangsang, int wahae, int talsun) {
    this.keeyee = keeyee;
    this.hwangak = hwangak;
    this.mangsang = mangsang;
    this.wahae = wahae;
    this.talsun = talsun;
  }

  public static List<WeeklyStatisticResponseDto> mock() {
    List<WeeklyStatisticResponseDto> list = new ArrayList<>();
    list.add(new WeeklyStatisticResponseDto(16, 14, 13, 12, 23));
    list.add(new WeeklyStatisticResponseDto(17, 13, 15, 11, 20));
    list.add(new WeeklyStatisticResponseDto(15, 16, 11, 14, 13));
    list.add(new WeeklyStatisticResponseDto(16, 12, 14, 13, 19));
    return list;
  }
}
