package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.service.dto.gpt.BehaviorRecognitionDto;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BehaviorService {

  private final BehaviorRepository behaviorRepository;
  private final OddBehaviorRepository oddBehaviorRepository;

  private final GptService gptService;
  private final PromptManager promptManager;

  @Transactional
  public void recognitionBehavior(String caption) {
    Behavior behavior = behaviorRepository.save(Behavior.builder()
        .caption(caption)
        .build());

    log.info("behavior={}", behavior);
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(promptManager.getRecognitionBehavior())
        .addUserMessage(caption)
        .build();

    BehaviorRecognitionDto response = gptService.ask(request, BehaviorRecognitionDto.class);
    log.info("recognition response={}", response);
    if (isOdd(response)) {
      OddBehavior oddBehavior = oddBehaviorRepository.save(OddBehavior.builder()
          .behavior(behavior)
          .reason(response.evidence())
          .build());
      log.info("oddBehavior={}", oddBehavior);
    }
  }

  private boolean isOdd(BehaviorRecognitionDto dto) {
    return dto.isDetected();
  }
}
