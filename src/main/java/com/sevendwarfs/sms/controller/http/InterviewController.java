package com.sevendwarfs.sms.controller.http;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/interviews", produces = "application/json; charset=UTF-8")
public class InterviewController {

  // 질문 목록 등록
  @PostMapping
  public ResponseEntity<InterviewResponseDto> createInterview(
      @RequestBody InterviewCreateDto dto

  ) {
    InterviewResponseDto saved;
    saved = InterviewResponseDto.mock(dto);
    return ResponseEntity.ok().body(saved);
  }

  // TODO : 질문 수정

  // TODO : 질문 삭제
}
