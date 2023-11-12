package com.sevendwarfs.sms.controller.stomp;

import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.OddRecognitionResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

  private final SimpMessagingTemplate template;

  public void send(String destination, Object message) {
    template.convertAndSend(destination, message);
  }

  public void sendStatistic(StatisticResponseDto res) {
    send("/topic/statistic", res);
  }

  public void sendMessage(Long id, String message,String sender) {
    send("/topic/chat", ChatResponseDto.builder()
        .id(id)
        .script(message)
        .sender(sender)
        .build());
  }

  public void sendOddMessageDetected(Long id) {
    send("/topic/odd_detect", OddRecognitionResponseDto.builder()
        .id(id)
        .build());
  }

  public void statisticMock() {
    send("/topic/statistic", StatisticResponseDto.mock());
  }
}
