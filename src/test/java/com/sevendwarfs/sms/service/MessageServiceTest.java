package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import java.util.Optional;
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
  OddMessageRepository oddMessageRepository;

  @Autowired
  DialogService dialogService;

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

  @Test
  @Transactional
  void 이상발화_메시지_생성_테스트() {
    String script = "i'm user";
    Long messageId = messageService.createAssistantMessage(script);

    MessageRecognitionDto recognition = new MessageRecognitionDto(true, true, true, true,
        "it's test");

    Long oddMessageId = messageService.createOddMessage(messageId, recognition);

    Optional<OddMessage> optionalOddMessage = oddMessageRepository.findById(oddMessageId);
    assertTrue(optionalOddMessage.isPresent());
    OddMessage oddMessage = optionalOddMessage.get();

    assertEquals(oddMessage.getMessage().getId(), messageId);
    assertEquals(oddMessage.getReason(), recognition.rationale());
    assertEquals(oddMessage.getIsDelusions(), recognition.delusions());
    assertEquals(oddMessage.getIsHallucination(), recognition.hallucination());
    assertEquals(oddMessage.getIsDisorganized(), recognition.disorganizedLanguage());
    assertEquals(oddMessage.getIsLinguisticDerailment(), recognition.linguisticDerailment());
  }

  @Test
  @Transactional
  void 이상발화_삭제_테스트() {
    Long userMessage = messageService.createUserMessage("test");
    Long oddMessage = messageService.createOddMessage(userMessage,
        new MessageRecognitionDto(true, true, true, true, "test"));

    messageService.deleteOddMessage(userMessage);

    Optional<OddMessage> optionalOddMessage = oddMessageRepository.findByMessageId(userMessage);
    assertTrue(optionalOddMessage.isEmpty());
  }
}