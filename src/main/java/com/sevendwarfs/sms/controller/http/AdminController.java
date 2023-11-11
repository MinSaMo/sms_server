package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final DialogService dialogService;

  @GetMapping("/dialog_end")
  public String endDialog() {
    dialogService.endDialog();
    return "OK";
  }
}
