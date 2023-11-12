package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import com.sevendwarfs.sms.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/interviews", produces = "application/json; charset=UTF-8")
public class InterviewController {

  private final InterviewService interviewService;
  // 질문 목록 등록

  @Operation(description = "인터뷰 리스트 조회 API\n"
      + "!!!!timestamp 형식은 : 11:15 이런 형식!!!!")
  @PostMapping
  public ResponseEntity<InterviewResponseDto> createInterview(
      @RequestBody InterviewCreateDto dto

  ) {
    InterviewResponseDto saved;
    saved = interviewService.createInterview(dto);
    interviewService.scheduleInterview(saved.getId());
    return ResponseEntity.ok().body(saved);
  }

  @Operation(description = "인터뷰 리스트 조회 API\n"
      + "!!!!timestamp 형식은 : 11:15 이런 형식!!!!")
  @GetMapping
  public List<InterviewResponseDto> getInterviewList() {
    return interviewService.findAll();
  }
}
