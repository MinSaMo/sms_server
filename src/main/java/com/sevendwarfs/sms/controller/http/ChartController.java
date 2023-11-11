package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.controller.http.dto.response.BehaviorDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.DialogDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.ChartService;
import com.sevendwarfs.sms.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/charts", produces = "application/json; charset=UTF-8")
public class ChartController {

  private final MessageService messageService;
  private final BehaviorService behaviorService;
  private final ChartService chartService;
  @GetMapping("/today")
  public ResponseEntity<ChartResponseDto> getChartOfToday() {
    ChartResponseDto response = chartService.generateChart();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/odd")
  public ResponseEntity<OddListResponseDto> getChartOfOdd() {
    OddListResponseDto response = chartService.generateOddList();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/message/detail/{messageId}")
  public ResponseEntity<DialogDetailResponseDto> getDialogByMessage(
      @PathVariable Long messageId
  ) {
    DialogDetailResponseDto response = messageService.getDialog(messageId);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/behavior/detail/{behaviorId}")
  public ResponseEntity<BehaviorDetailResponseDto> getVideoIdByBehavior(
      @PathVariable Long behaviorId
  ) {
    BehaviorDetailResponseDto response = behaviorService.getBehaviorDetail(behaviorId);
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/behavior/{behaviorId}")
  public ResponseEntity deleteOddBehavior(
      @PathVariable Long behaviorId
  ) {
    behaviorService.deleteOddBehavior(behaviorId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/message/{messageId}")
  public ResponseEntity deleteOddMessage(
      @PathVariable Long messageId
  ) {
    messageService.deleteOddMessage(messageId);
    return ResponseEntity.noContent().build();
  }
}
