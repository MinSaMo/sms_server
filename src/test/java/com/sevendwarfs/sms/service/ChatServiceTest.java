package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.enums.MessageClassification;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {

  @Autowired
  ChatService chatService;
  @Autowired
  MessageService messageService;
  @Autowired
  OddMessageRepository oddMessageRepository;

  @BeforeEach
  void beforeEach() {
    oddMessageRepository.deleteAll();
  }
  @Test
  void 발화_의도_테스트_NOT_SUPPORTED() {
    String message = "Can you see the pattern on this dollar bill?";
    MessageClassification classification = chatService.classifyMessage(message);

    assertEquals(classification, MessageClassification.NOT_SUPPORTED);
  }

  @Test
  void 발화_의도_테스트_SUPPORTED() {
    String message = "Hello";
    MessageClassification classification = chatService.classifyMessage(message);

    assertEquals(classification, MessageClassification.SUPPORTED);
  }

  @Test
  void 응답_테스트() {

    String message = "매운 음식 추천해줘";
    String reply = chatService.replyToMessage(message);
  }

  @Test
  @Transactional
  void 이상발화_판단_테스트_정상케이스() {
    String script = "안녕";
    Long messageId = messageService.createUserMessage(script);

    Optional<Long> optionalOdd = chatService.recognizeMessage(messageId);
    assertTrue(optionalOdd.isEmpty());
  }

  @Test
  @Transactional
  void 이상발화_판단_테스트_이상발화_케이스() {
    String script = "달그락탈그락 우우을탈락라라락";
    Long messageId = messageService.createUserMessage(script);

    Optional<Long> optionalOdd = chatService.recognizeMessage(messageId);
    assertTrue(optionalOdd.isPresent());
    Long oddId = optionalOdd.get();

    Optional<OddMessage> optionalOddMessage = oddMessageRepository.findById(oddId);
    assertTrue(optionalOddMessage.isPresent());
    OddMessage oddMessage = optionalOddMessage.get();
    boolean isOdd = oddMessage.getIsDisorganized() || oddMessage.getIsDelusions() ||
        oddMessage.getIsHallucination() || oddMessage.getIsLinguisticDerailment();
    assertTrue(isOdd);
    assertFalse(oddMessage.getReason().isEmpty());
  }
}