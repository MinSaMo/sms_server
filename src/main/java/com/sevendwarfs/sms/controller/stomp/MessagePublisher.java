package com.sevendwarfs.sms.controller.stomp;

import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.MessageClassifyResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.MessageModifiedResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

  private final SimpMessagingTemplate template;

  private final String classify[] = {"환자의 정상적 대화로 판단", "환자의 비정상적 대화로 대화 차단"};


  public void send(String destination, Object message) {
    template.convertAndSend(destination, message);
  }

  public void sendStatistic(StatisticResponseDto res) {
    send("/topic/statistic", res);
  }

  public void sendMessage(Long id, String message,String sender) {
    send("/topic/chat", ChatResponseDto.builder()
        .id(id)
        .isOdd(false)
        .script(message)
        .sender(sender)
        .build());
  }

  public void sendClassification(Long id, String script, int val) {
    send("/topic/classify", MessageClassifyResponseDto.builder()
        .id(id)
        .script(script)
        .classification(classify[val])
        .build());
  }

  public void sendMessageModified(Long id,boolean isOdd) {
    send("/topic/message_modified", MessageModifiedResponseDto.builder()
        .id(id)
        .isOdd(isOdd)
        .build());
  }

  public void statisticMock() {
    send("/topic/statistic", StatisticResponseDto.mock());
  }
}
