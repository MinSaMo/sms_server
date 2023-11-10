package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final OddMessageRepository oddMessageRepository;
  private final DialogService dialogService;

  @Transactional
  public Long createUserMessage(String script) {
    Message message = messageRepository.save(Message.userMessage(script));
    message.setDialog(dialogService.getCurrentDialog());
    log.info("created user message={}", message);
    return message.getId();
  }

  @Transactional
  public Long createAssistantMessage(String script) {
    Message message = messageRepository.save(Message.assistantMessage(script));
    message.setDialog(dialogService.getCurrentDialog());
    log.info("created assistant message={}", message);
    return message.getId();
  }

  @Transactional
  public Long createOddMessage(Long messageId, MessageRecognitionDto recognition) {
    Message message = findById(messageId);
    OddMessage oddMessage = oddMessageRepository.save(OddMessage.builder()
        .message(message)
        .recognition(recognition)
        .build());
    log.info("created odd Message={}", oddMessage);
    return oddMessage.getId();
  }

  @Transactional
  public Message findById(Long messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("Not found Message"));
  }
}
