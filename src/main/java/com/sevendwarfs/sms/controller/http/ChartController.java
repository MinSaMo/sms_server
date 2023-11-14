package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.controller.http.dto.response.BehaviorDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.DialogDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.ChartService;
import com.sevendwarfs.sms.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
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

  @Operation(description = "금일 차트 조회 API")
  @GetMapping("/today")
  public ResponseEntity<ChartResponseDto> getChartOfToday() {
    ChartResponseDto response = chartService.generateChart();
    return ResponseEntity.ok().body(response);
  }

  @Operation(description = "이상발화,행동 조회 API")
  @GetMapping("/odd")
  public ResponseEntity<OddListResponseDto> getChartOfOdd() {
    OddListResponseDto response = chartService.generateOddList();
    return ResponseEntity.ok().body(response);
  }

  @Operation(description = "이상발화 상세 대화 내용 조회 API")
  @GetMapping("/message/detail/{messageId}")
  public ResponseEntity<DialogDetailResponseDto> getDialogByMessage(
      @PathVariable Long messageId
  ) {
    DialogDetailResponseDto response = messageService.getDialog(messageId);
    return ResponseEntity.ok().body(response);
  }

  @Operation(description = "이상행동 비디오 id 조회 API")
  @GetMapping("/behavior/detail/{behaviorId}")
  public ResponseEntity<BehaviorDetailResponseDto> getVideoIdByBehavior(
      @PathVariable Long behaviorId
  ) {
    BehaviorDetailResponseDto response = behaviorService.getBehaviorDetail(behaviorId);
    return ResponseEntity.ok().body(response);
  }

  @Operation(description = "차트 xlsx 다운로드 API")
  @GetMapping("/today/download")
  public void xlsxDownload(HttpServletResponse response) {
    response.setHeader("Content-Disposition", "attachment;filename=testExcel1.xls");
    response.setContentType("application/octet-stream");

    Workbook workbook = chartService.makeXLSXFile();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      workbook.write(outputStream);
      ByteArrayInputStream stream = new ByteArrayInputStream(
          outputStream.toByteArray());
      IOUtils.copy(stream, response.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Operation(description = "이상행동 삭제 API")
  @DeleteMapping("/behavior/{behaviorId}")
  public ResponseEntity deleteOddBehavior(
      @PathVariable Long behaviorId
  ) {

    behaviorService.deleteOddBehavior(behaviorId);
    return ResponseEntity.noContent().build();
  }

  @Operation(description = "이상발화 삭제 API")
  @DeleteMapping("/message/{messageId}")
  public ResponseEntity deleteOddMessage(
      @PathVariable Long messageId
  ) {
    messageService.deleteOddMessage(messageId);
    return ResponseEntity.noContent().build();
  }
}
