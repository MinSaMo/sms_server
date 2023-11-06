package com.sevendwarfs.sms.controller.http.dto.response;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.domain.Interview;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class InterviewResponseDto {

  private Long id;
  private String question;
  private LocalDateTime questionTime;

  @Builder
  public InterviewResponseDto(Long id, String question, LocalDateTime questionTime) {
    this.id = id;
    this.question = question;
    this.questionTime = questionTime;
  }

  public static InterviewResponseDto mock(InterviewCreateDto dto) {
    return InterviewResponseDto.builder()
        .id(1L)
        .question(dto.question())
        .questionTime(dto.time())
        .build();
  }

  public static InterviewResponseDto of(Interview obj) {
    return InterviewResponseDto.builder()
        .id(obj.getId())
        .question(obj.getQuestion())
        .questionTime(obj.getQuestionTime())
        .build();
  }
}
