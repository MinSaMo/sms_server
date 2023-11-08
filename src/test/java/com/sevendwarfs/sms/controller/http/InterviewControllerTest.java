package com.sevendwarfs.sms.controller.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InterviewControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  final String domain = "/interviews";

  @Test
  void 인터뷰_생성_응답_테스트() throws Exception {

    String question = "약을 복용 하셨나요?";
    LocalDateTime time = LocalDateTime.now();

    InterviewCreateDto testDto;
    testDto = new InterviewCreateDto(question, time);

    String content = objectMapper.writeValueAsString(testDto);
    String result = mockMvc.perform(post(domain)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(
            status().isOk()
        )
        .andReturn().getResponse().getContentAsString();

    InterviewResponseDto response = objectMapper.readValue(result,
        InterviewResponseDto.class);

    assertEquals(question, response.getQuestion());
    assertEqualsOfTime(time, response.getQuestionTime());
  }

  void assertEqualsOfTime(LocalDateTime t1, LocalDateTime t2) {
    assertEquals(t1.getYear(), t2.getYear());
    assertEquals(t1.getMonth(), t2.getMonth());
    assertEquals(t1.getDayOfMonth(), t2.getDayOfMonth());
    assertEquals(t1.getHour(), t2.getHour());
    assertEquals(t1.getMinute(), t2.getMinute());
    assertEquals(t1.getSecond(), t2.getSecond());
  }
}