package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/charts", produces = "application/json; charset=UTF-8")
public class ChartController {

  // 금일 차팅 목록 (간단)
  @GetMapping("/today_abbreviation")
  public ResponseEntity<ChartResponseDto> getChartOfToday() {
    return ResponseEntity.ok().body(ChartResponseDto.mock());
  }

  // 이상행동, 이상발화 리스트
  @GetMapping("/odd")
  public ResponseEntity<OddListResponseDto> getChartOfOdd() {
    return ResponseEntity.ok().body(OddListResponseDto.mock());
  }
}
