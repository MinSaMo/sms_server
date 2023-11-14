package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.service.dto.gpt.MessageClassificationDto;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import com.sevendwarfs.sms.service.dto.gpt.GptMessageResponseDto;
import com.sevendwarfs.sms.service.enums.MessageClassification;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

  private final GptService gptService;
  private final PromptManager promptManager;
  private final MessageService messageService;
  private final DialogService dialogService;

  public MessageClassification classifyMessage(String message) {
    Prompt prompt = promptManager.getClassifyMessage();
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(prompt.getScript())
        .addUserMessage(message)
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();
    MessageClassificationDto response = gptService.ask(request, MessageClassificationDto.class);
    MessageClassification classification = MessageClassification.of(
        response.classification());
    log.info("message={}, classification={}", message, classification);
    return classification;
  }

  public String replyToMessage(String message) {
    Prompt prompt = promptManager.getDaily();
    String summary = String.format("summary : %s", dialogService.getRecentSummary());
    ChatCompletionRequest request = dialogService.addCurrentDialog(
            gptService.request())
        .addSystemMessage(prompt.getScript())
        .addUserMessage(summary)
        .addUserMessage(message)
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();

    GptMessageResponseDto response = gptService.ask(request, GptMessageResponseDto.class);
    log.info("message={}, response={}", message, response.script());
    return response.script();
  }

  @Transactional
  public Optional<Long> recognizeMessage(Long messageId) {
    Message message = messageService.findById(messageId);

    Prompt prompt = promptManager.getRecognitionMessage();
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(prompt.getScript())
        .addUserMessage(message.getContent())
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();

    MessageRecognitionDto response = gptService.askToSub(request, MessageRecognitionDto.class);
    log.info("recognize message result={}", response);
    if (isOdd(response)) {
      messageService.createOddMessage(messageId, response);
      return Optional.of(messageId);
    }
    return Optional.empty();
  }

  private boolean isOdd(MessageRecognitionDto dto) {
    return dto.delusions() || dto.hallucination() || dto.disorganizedLanguage()
        || dto.linguisticDerailment();
  }
}
