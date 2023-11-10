package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.service.dto.gpt.MessageClassificationDto;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import com.sevendwarfs.sms.service.dto.gpt.MessageResponseDto;
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

  public MessageClassification classifyMessage(String message) {
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(promptManager.getClassifyMessage())
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

  @Transactional
  public Optional<Long> recognizeMessage(Long messageId) {
    Message message = messageService.findById(messageId);

    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(promptManager.getRecognitionMessage())
        .addUserMessage(message.getContent())
        .build();

    MessageRecognitionDto response = gptService.ask(request, MessageRecognitionDto.class);
    log.info("recognize message result={}", response);
    if (isOdd(response)) {
      Long oddId = messageService.createOddMessage(messageId, response);
      return Optional.of(oddId);
    }
    return Optional.empty();
  }

  private boolean isOdd(MessageRecognitionDto dto) {
    return dto.delusions() || dto.hallucination() || dto.disorganizedLanguage()
        || dto.linguisticDerailment();
  }
}
