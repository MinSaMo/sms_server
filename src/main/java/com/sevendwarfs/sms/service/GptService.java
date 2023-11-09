package com.sevendwarfs.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ai4j.openai4j.Model;
import dev.ai4j.openai4j.OpenAiClient;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import dev.ai4j.openai4j.chat.ChatCompletionResponse;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptService {

  @Getter(AccessLevel.PROTECTED)
  private final OpenAiClient mainClient;
  @Getter(AccessLevel.PROTECTED)
  private final OpenAiClient subClient;
  private final ObjectMapper objectMapper;
  private final PromptManager promptManager;

  private final long timeout = 60;

  @Autowired
  public GptService(
      @Value("${openai.key.main}") String mainKey,
      @Value("${openai.key.sub}") String subKey,
      ObjectMapper objectMapper, PromptManager promptManager) {
    mainClient = OpenAiClient.builder()
        .openAiApiKey(mainKey)
        .callTimeout(Duration.ofSeconds(timeout))
        .build();
    subClient = OpenAiClient.builder()
        .openAiApiKey(subKey)
        .callTimeout(Duration.ofSeconds(timeout))
        .build();
    this.objectMapper = objectMapper;
    this.promptManager = promptManager;
  }

  public String ask(ChatCompletionRequest request) {
    log.info("gpt-request-message : {}", request.messages());
    ChatCompletionResponse response = executeOpenAI(mainClient, request);
    log.info("gpt-response : {}", response);
    return getContent(response);
  }

  protected String getContent(ChatCompletionResponse response) {
    return response.choices().get(0).message().content();
  }

  protected ChatCompletionResponse executeOpenAI(OpenAiClient client,
      ChatCompletionRequest request) {
    return client.chatCompletion(request).execute();
  }

  public <T> T ask(ChatCompletionRequest request, Class<T> clazz) {
    String content = this.ask(request);
    log.info("response-type : {}", clazz.getName());
    if (clazz.equals(String.class)) {
      return clazz.cast(content);
    }
    try {
      return objectMapper.readValue(content, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public ChatCompletionRequest.Builder request() {
    return ChatCompletionRequest.builder()
        .model(Model.GPT_3_5_TURBO)
        .addSystemMessage(promptManager.getGlobal());
  }
}
