package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.service.dto.gpt.MessageClassificationDto;
import com.sevendwarfs.sms.service.dto.gpt.MessageResponseDto;
import com.sevendwarfs.sms.service.enums.MessageClassification;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

  private final GptService gptService;
  private final PromptManager promptManager;

  public MessageClassification classifyMessage(String message) {
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(promptManager.getMessageClassify())
        .addUserMessage(message)
        .build();
    MessageClassificationDto response = gptService.ask(request, MessageClassificationDto.class);
    MessageClassification classification = MessageClassification.of(
        response.classification());
    log.info("message={}, classification={}", message, classification);
    return classification;
  }

  public String replyToMessage(String message) {
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(promptManager.getDaily())
        .addUserMessage(message)
        .build();
    MessageResponseDto response = gptService.ask(request, MessageResponseDto.class);
    log.info("message={}, response={}", message, response.script());
    return response.script();
  }
}
