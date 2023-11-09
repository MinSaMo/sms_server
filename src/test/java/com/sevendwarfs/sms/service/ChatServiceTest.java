package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sevendwarfs.sms.service.enums.MessageClassification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {

  @Autowired
  ChatService chatService;

  @Test
  void 발화_의도_테스트() {
    String message = "Can you see the pattern on this dollar bill?";
    MessageClassification classification = chatService.classifyMessage(message);

    assertEquals(classification,MessageClassification.NOT_SUPPORTED);
  }

  @Test
  void 응답_테스트() {

    String message = "매운 음식 추천해줘";
    String reply = chatService.replyToMessage(message);
  }
}