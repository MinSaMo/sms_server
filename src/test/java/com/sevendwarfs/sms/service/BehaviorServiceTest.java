package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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


  @BeforeEach
  void beforeEach() {
    behaviorRepository.deleteAll();
    oddBehaviorRepository.deleteAll();
  }

  @Test
  @Transactional
  void 이상행동_감지_테스트_정상케이스() {
    String caption = "A main sitting down on a chair";
    behaviorService.recognitionBehavior(caption);

    Optional<Behavior> optionalBehavior = behaviorRepository.findByCaption(caption);
    assertTrue(optionalBehavior.isPresent());

    Behavior behavior = optionalBehavior.get();
    Optional<OddBehavior> optionalOddBehavior = oddBehaviorRepository.findByBehaviorId(behavior.getId());
    assertFalse(optionalOddBehavior.isPresent());
  }

  @Test
  @Transactional
  void 이상행동_감지_테스트_비정상케이스() {
    String caption = "A person is seen laughing hysterically at a somber event without any apparent trigger.";
    behaviorService.recognitionBehavior(caption);

    Optional<Behavior> optionalBehavior = behaviorRepository.findByCaption(caption);
    assertTrue(optionalBehavior.isPresent());

    Behavior behavior = optionalBehavior.get();
    Optional<OddBehavior> optionalOddBehavior = oddBehaviorRepository.findByBehaviorId(behavior.getId());
    assertTrue(optionalOddBehavior.isPresent());
  }
}