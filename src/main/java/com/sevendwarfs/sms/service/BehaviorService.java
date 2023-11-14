package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.response.BehaviorDetailResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.BehaviorResponseDto;
import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.service.dto.gpt.BehaviorRecognitionDto;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
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

    BehaviorRecognitionDto response = gptService.askToSub(request, BehaviorRecognitionDto.class);
    log.info("recognition response={}", response);
    if (isOdd(response)) {
      OddBehavior oddBehavior = createOddBehavior(behavior, response.reason(), videoId);
      log.info("oddBehavior={}", oddBehavior);
      return true;
    }
    return false;
  }

  @Transactional
  public OddBehavior createOddBehavior(Behavior behavior, String reason, Long videoId) {
    return oddBehaviorRepository.save(OddBehavior.builder()
        .behavior(behavior)
        .reason(reason)
        .videoId(videoId)
        .build());
  }

  @Transactional
  public void deleteOddBehavior(Long behaviorId) {
    oddBehaviorRepository.deleteByBehaviorId(behaviorId);
  }

  @Transactional
  public Behavior createBehavior(String caption) {
    return behaviorRepository.save(Behavior.builder()
        .caption(caption)
        .build());
  }

  @Transactional
  public List<BehaviorResponseDto> getTodayBehaviorChart() {
    List<OddBehavior> oddBehaviorList = findTodayOddBehavior();
    List<Behavior> behaviorList = findTodayBehavior();

    return behaviorList.stream()
        .map(behavior -> BehaviorResponseDto.builder()
            .caption(behavior.getCaption())
            .isOdd(isOddBehavior(behavior, oddBehaviorList))
            .timestamp(behavior.getTimestamp())
            .build())
        .sorted((b1, b2) -> b1.getTimestamp().isAfter(b2.getTimestamp()) ? 1 : -1)
        .toList();
  }

  private List<Behavior> findTodayBehavior() {
    return behaviorRepository.findByTimestampBetween(startOfDay(),
        endOfDay());
  }

  private boolean isOddBehavior(Behavior behavior, List<OddBehavior> oddBehaviorList) {
    for (OddBehavior odd : oddBehaviorList) {
      if (odd.getBehavior().equals(behavior)) {
        return true;
      }
    }
    return false;
  }

  @Transactional
  public List<OddBehavior> findOddBehaviorWeekly() {
    return oddBehaviorRepository.findByBehaviorTimestampBetween(startOfWeek(), endOfWeek());
  }

  @Transactional
  public List<OddBehavior> findTodayOddBehavior() {
    LocalDateTime start = startOfDay();
    LocalDateTime end = endOfDay();
    return oddBehaviorRepository.findByBehaviorTimestampBetween(start, end).stream()
        .sorted(
            (b1, b2) -> b1.getBehavior().getTimestamp()
                .isAfter(b2.getBehavior().getTimestamp()) ? 1 : -1)
        .toList();
  }

  @Transactional
  public BehaviorDetailResponseDto getBehaviorDetail(Long behaviorId) {
    Behavior behavior = findById(behaviorId);
    OddBehavior oddBehavior = oddBehaviorRepository.findByBehaviorId(behaviorId)
        .orElseThrow(() -> new RuntimeException("OddBehavior Not found"));

    return BehaviorDetailResponseDto.builder()
        .behaviorId(behaviorId)
        .caption(behavior.getCaption())
        .videoId(oddBehavior.getVideoId())
        .build();
  }

  @Transactional
  public Behavior findById(Long behaviorId) {
    return behaviorRepository.findById(behaviorId)
        .orElseThrow(() -> new RuntimeException("Behavior Not found"));
  }


  protected LocalDateTime startOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
  }

  protected LocalDateTime startOfWeek() {
    return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).withHour(0)
        .withMinute(0).withSecond(0).withNano(0);
  }


  protected LocalDateTime endOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
  }

  protected LocalDateTime endOfWeek() {
    return LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
  }


  private boolean isOdd(BehaviorRecognitionDto dto) {
    return dto.isDetected();
  }
}
