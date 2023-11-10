package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class MessageServiceTest {

  @Autowired
  MessageService messageService;

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  DialogService dialogService;

  @BeforeEach
  void beforeEach() {
    messageRepository.deleteAll();
  }

  @Test
  @Transactional
  void 사용자_메시지_생성_테스트() {
    String script = "i'm user";
    Long messageId = messageService.createUserMessage(script);
    Long dialogId = dialogService.getCurrentDialog().getId();

    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    assertTrue(optionalMessage.isPresent());
    Message message = optionalMessage.get();

    assertEquals(message.getSender(), Message.USER);
    assertEquals(message.getContent(), script);
    assertEquals(message.getDialog().getId(), dialogId);
  }

  @Test
  @Transactional
  void 다일라_메시지_생성_테스트() {
    String script = "i'm user";
    Long messageId = messageService.createAssistantMessage(script);
    Long dialogId = dialogService.getCurrentDialog().getId();

    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    assertTrue(optionalMessage.isPresent());
    Message message = optionalMessage.get();

    assertEquals(message.getSender(), Message.ASSISTANT);
    assertEquals(message.getContent(), script);
    assertEquals(message.getDialog().getId(), dialogId);
  }

}