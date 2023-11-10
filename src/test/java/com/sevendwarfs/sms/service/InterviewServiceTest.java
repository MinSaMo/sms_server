package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewRepository;
import com.sevendwarfs.sms.global.exception.GptRemoteServerError;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class InterviewServiceTest {

  @Autowired
  InterviewService interviewService;

  @Autowired
  InterviewRepository interviewRepository;

  @BeforeEach
  void beforeEach() {
    interviewRepository.deleteAll();
  }

  @Test
  @Transactional
  void 인터뷰_생성_테스트() {
    String question = "test interview";
    LocalTime time = LocalTime.now();

    InterviewResponseDto response = interviewService.createInterview(
        new InterviewCreateDto(question, time));

    assertNotNull(response.getId());
    assertEquals(response.getQuestion(), question);
    assertTrue(isEqualTime(response.getQuestionTime(), time));

    Optional<Interview> optionalInterview = interviewRepository.findById(response.getId());
    assertTrue(optionalInterview.isPresent());
    Interview interview = optionalInterview.get();
    assertNotNull(interview.getId());
    assertEquals(interview.getQuestion(), question);
    assertTrue(isEqualTime(interview.getQuestionTime(), time));
    assertEquals(interview.getDialogList().size(), 0);
  }

  @Test
  @Transactional
  void 인터뷰_질문_생성_테스트() {
    try {
      String question = "소염제 복용 확인";
      LocalTime time = LocalTime.now();

      InterviewResponseDto saved = interviewService.createInterview(
          new InterviewCreateDto(question, time));

      Long interviewId = saved.getId();

      String response = interviewService.generateInterviewScript(interviewId);
      assertFalse(response.isEmpty());
    } catch (GptRemoteServerError error) {
      return;
    }
  }

  boolean isEqualTime(LocalTime t1, LocalTime t2) {
    boolean sameHour = t1.getHour() == t2.getHour();
    boolean sameMinute = t1.getMinute() == t2.getMinute();
    return sameHour && sameMinute;
  }
}