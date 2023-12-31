package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.global.exception.GptRemoteServerError;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class BehaviorServiceTest {

  @Autowired
  BehaviorService behaviorService;

  @Autowired
  BehaviorRepository behaviorRepository;
  @Autowired
  OddBehaviorRepository oddBehaviorRepository;


  Long videoId = 1L;

  @Test
  @Transactional
  void 이상행동_감지_테스트_정상케이스() {
    String caption = "A main sitting down on a chair";
    try {
      behaviorService.recognitionBehavior(caption, videoId);
    } catch (GptRemoteServerError e) {
      return;
    }
    Optional<Behavior> optionalBehavior = behaviorRepository.findByCaption(caption);

    assertTrue(optionalBehavior.isPresent());

    Behavior behavior = optionalBehavior.get();
    Optional<OddBehavior> optionalOddBehavior = oddBehaviorRepository.findByBehaviorId(
        behavior.getId());
    assertFalse(optionalOddBehavior.isPresent());
  }

  @Test
  @Transactional
  void 이상행동_감지_테스트_비정상케이스() {
    String caption = "A person is seen laughing hysterically at a somber event without any apparent trigger.";
    try {
      behaviorService.recognitionBehavior(caption, videoId);
    } catch (GptRemoteServerError e) {
      return;
    }

    Optional<Behavior> optionalBehavior = behaviorRepository.findByCaption(caption);
    assertTrue(optionalBehavior.isPresent());

    Behavior behavior = optionalBehavior.get();
    Optional<OddBehavior> optionalOddBehavior = oddBehaviorRepository.findByBehaviorId(
        behavior.getId());
    assertTrue(optionalOddBehavior.isPresent());
  }

  @Test
  @Transactional
  void 이상행동_삭제_테스트() {
    Behavior behavior = behaviorService.createBehavior("test");
    OddBehavior odd = behaviorService.createOddBehavior(behavior, "reason", 1L);
    Long id = behavior.getId();
    behaviorService.deleteOddBehavior(id);

    Optional<OddBehavior> optionalOddBehavior = oddBehaviorRepository.findByBehaviorId(id);
    assertTrue(optionalOddBehavior.isEmpty());
  }

  @Test
  @Transactional
  void 주차별_테스트() {
    Behavior behavior = behaviorService.createBehavior("test");
    behavior.setTimestamp(LocalDateTime.now().withDayOfMonth(10));
    OddBehavior oddBehavior = OddBehavior.builder()
        .videoId(1L)
        .behavior(behavior)
        .reason("test")
        .build();
    oddBehaviorRepository.save(oddBehavior);

    List<OddBehavior> list = behaviorService.findOddBehaviorWeekly();
    assertTrue(list.isEmpty());
  }
}