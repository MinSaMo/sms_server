package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.response.DialogDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.MessageResponseDto;
import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
  public void deleteOddMessage(Long messageId) {
    oddMessageRepository.deleteByMessageId(messageId);
  }

  @Transactional
  public Message findById(Long messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("Not found Message"));
  }

  @Transactional
  public List<OddMessage> findAllOddMessage() {
    return oddMessageRepository.findAll();
  }


  @Transactional
  public List<OddMessage> findTodayOddMessage() {
    LocalDateTime start = startOfDay();
    LocalDateTime end = endOfDay();
    return oddMessageRepository.findByMessageTimestampBetween(start, end);
  }

  @Transactional
  public DialogDetailResponseDto getDialog(Long messageId) {
    Message message = findById(messageId);
    Dialog dialog = message.getDialog();
    List<MessageResponseDto> messageList = dialog.getMessageList().stream()
        .map(msg -> {
          if (msg.getId().equals(messageId)) {
            return MessageResponseDto.odd(msg);
          } else {
            return MessageResponseDto.chat(msg);
          }
        })
        .toList();
    return DialogDetailResponseDto.builder()
        .oddMessage(message.getContent())
        .chats(messageList)
        .build();
  }

  protected LocalDateTime startOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
  }

  protected LocalDateTime endOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
  }

}
