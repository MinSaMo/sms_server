package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sevendwarfs.sms.global.exception.GptRemoteServerError;
import dev.ai4j.openai4j.OpenAiClient;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import dev.ai4j.openai4j.chat.ChatCompletionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GptServiceTest {

  static class Response {

    public String response;
    public Boolean isTrue;
    public Integer score;
  }

  @Autowired
  GptService gptService;

  public boolean isApiExceptionOccur;

  @BeforeEach
  void beforeEach() {
    isApiExceptionOccur = false;
  }

  // TODO : GPT Exception 터질때 처리
  String executeTestAPI(OpenAiClient client) {
    ChatCompletionRequest request = gptService.request()
        .addUserMessage("hello")
        .build();
    ChatCompletionResponse response;
    try {
      response = gptService.executeOpenAI(client, request);
    } catch (GptRemoteServerError e) {
      isApiExceptionOccur = true;
      return null;
    }
    return gptService.getContent(response);
  }

  <T> T executeAsk(ChatCompletionRequest request, Class<T> clazz) {
    try {
      return gptService.ask(request, clazz);
    } catch (GptRemoteServerError e) {
      isApiExceptionOccur = true;
      return null;
    }
  }

  @Test
  void MAIN_KEY_CLIENT_동작_확인() {
    OpenAiClient mainClient = gptService.getMainClient();
    String content = executeTestAPI(mainClient);
    if (!isApiExceptionOccur) {
      assertFalse(content.isEmpty());
    }
  }

  @Test
  void SUB_KEY_CLIENT_동작_확인() {
    OpenAiClient mainClient = gptService.getSubClient();
    String content = executeTestAPI(mainClient);
    if (!isApiExceptionOccur) {
      assertFalse(content.isEmpty());
    }
  }

  @Test
  void 응답_포맷_변환_테스트_STRING() {
    ChatCompletionRequest request = gptService.request()
        .addUserMessage("hello")
        .build();
    String response = executeAsk(request, String.class);
    if (!isApiExceptionOccur) {
      assertFalse(response.isBlank());
    }
  }

  @Test
  void 응답_포맷_변환_테스트_OBJ() {
    String format = "{\"response\":<your reply>, \"isTrue\":<true or false>,\"score\":<random int in range of 1~10>}";
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage("Please answer with this json format : " + format)
        .addUserMessage("hello")
        .build();

    Response response = executeAsk(request, Response.class);
    if (!isApiExceptionOccur) {
      assertNotNull(response);
      assertNotNull(response.isTrue);
      assertNotNull(response.response);
      assertNotNull(response.score);
    }
  }
}