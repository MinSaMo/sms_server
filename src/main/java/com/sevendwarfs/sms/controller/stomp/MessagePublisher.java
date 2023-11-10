package com.sevendwarfs.sms.controller.stomp;

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

  public void statisticMock() {
    send("/topic/statistic", StatisticResponseDto.mock());
  }
}
