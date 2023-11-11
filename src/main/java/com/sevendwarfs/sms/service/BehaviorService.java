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
  public boolean recognitionBehavior(String caption, Long videoId) {
    Behavior behavior = createBehavior(caption);

    log.info("behavior={}", behavior);
    Prompt prompt = promptManager.getRecognitionBehavior();
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(prompt.getScript())
        .addUserMessage(caption)
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();

    BehaviorRecognitionDto response = gptService.ask(request, BehaviorRecognitionDto.class);
    log.info("recognition response={}", response);
    if (isOdd(response)) {
      OddBehavior oddBehavior = createOddBehavior(behavior, response.reason(), videoId);
      log.info("oddBehavior={}", oddBehavior);
      return true;
    }
    return false;
  }

  @Transactional
  public OddBehavior createOddBehavior(Behavior behavior, String reason,Long videoId) {
    return oddBehaviorRepository.save(OddBehavior.builder()
        .behavior(behavior)
        .reason(reason)
        .videoId(videoId)
        .build());
  }

  @Transactional
  public Behavior createBehavior(String caption) {
    return behaviorRepository.save(Behavior.builder()
        .caption(caption)
        .build());
  }

  private boolean isOdd(BehaviorRecognitionDto dto) {
    return dto.isDetected();
  }
}
