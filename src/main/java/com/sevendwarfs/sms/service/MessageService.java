package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.response.DialogDetailResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.MessageResponseDto;
import com.sevendwarfs.sms.controller.stomp.MessagePublisher;
import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
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
public class MessageService {

  private final MessageRepository messageRepository;
  private final OddMessageRepository oddMessageRepository;
  private final DialogService dialogService;
  private final MessagePublisher messagePublisher;

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
    messagePublisher.sendMessageModified(messageId, false);
  }

  @Transactional
  public Message findById(Long messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("Not found Message"));
  }

  @Transactional
  public List<OddMessage> findOddMessageWeekly() {
    return oddMessageRepository.findByMessageTimestampBetween(startOfWeek(), endOfWeek());
  }

  @Transactional
  public List<ChatResponseDto> getTodayChat() {

    List<Message> messageList = findTodayMessage();
    List<OddMessage> oddMessageList = findTodayOddMessage();

    return messageList.stream()
        .map(msg -> ChatResponseDto.builder()
            .id(msg.getId())
            .isOdd(isOddMessage(msg, oddMessageList))
            .sender(msg.getSender())
            .script(msg.getContent())
            .timestamp(msg.getTimestamp())
            .build())
        .sorted((m1, m2) -> m1.getTimestamp().isAfter(m2.getTimestamp()) ? 1 : -1)
        .toList();
  }

  private List<Message> findTodayMessage() {
    return messageRepository.findByTimestampBetween(startOfDay(),
        endOfDay());
  }

  private boolean isOddMessage(Message message, List<OddMessage> oddMessageList) {
    for (OddMessage oddMessage : oddMessageList) {
      if (oddMessage.getMessage().equals(message)) {
        return true;
      }
    }
    return false;
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

}
