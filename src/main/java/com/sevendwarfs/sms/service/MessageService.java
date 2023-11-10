package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddMessageRepository;
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
}
