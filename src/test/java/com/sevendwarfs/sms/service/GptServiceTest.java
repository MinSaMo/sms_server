package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GptServiceTest {

  @Autowired
  GptService gptService;

  @Test
  void 응답_포맷_변환_테스트_STRING() {
    ChatCompletionRequest request = gptService.request()
        .addUserMessage("hello")
        .build();
    String response = gptService.ask(request, String.class);

    assertFalse(response.isBlank());
  }

  @Test
  void 응답_포맷_변환_테스트_OBJ() {
    String format = "{response:<your reply>, isTrue:<true or false>,score:<random int in range of 1~10>}";
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage("Please answer with this json format : " + format)
        .addUserMessage("hello")
        .build();

    Response response = gptService.ask(request, Response.class);

    assertNotNull(response);
    assertNotNull(response.isTrue);
    assertNotNull(response.response);
    assertNotNull(response.score);
  }

  static class Response {
    public String response;
    public Boolean isTrue;
    public Integer score;
  }
}