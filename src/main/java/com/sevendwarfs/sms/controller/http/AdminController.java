package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.config.DatabaseConfigure;
import com.sevendwarfs.sms.service.DialogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final DialogService dialogService;
  private final DatabaseConfigure configure;

  @Operation(description = "대화 종료 admin api")
  @GetMapping("/dialog_end")
  public ResponseEntity endDialog() {
    dialogService.endDialog();
    return ResponseEntity.noContent().build();
  }

  @Operation(description = "DB 초기화 api")
  @GetMapping("/db_init")
  public ResponseEntity initDatabase() {
    configure.init();
    return ResponseEntity.noContent().build();
  }
}
