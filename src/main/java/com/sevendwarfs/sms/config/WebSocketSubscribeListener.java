package com.sevendwarfs.sms.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevendwarfs.sms.controller.stomp.MessagePublisher;
import com.sevendwarfs.sms.controller.stomp.dto.response.BehaviorResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.MessageService;
import com.sevendwarfs.sms.service.StatisticService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Configuration
@RequiredArgsConstructor
public class WebSocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

  private final ObjectMapper objectMapper;
  private final MessagePublisher messagePublisher;

  private final MessageService messageService;
  private final BehaviorService behaviorService;
  private final StatisticService statisticService;

  private final String CHAT = "/topic/chat";
  private final String BEHAVIOR = "/topic/behavior";
  private final String STAT = "/topic/statistic";

  @Override
  public void onApplicationEvent(SessionSubscribeEvent event) {
    MessageHeaders headers = event.getMessage().getHeaders();
    Map<String, Object> nativeHeaders = objectMapper.convertValue(headers.get("nativeHeaders"),
        new TypeReference<>() {
        });
    Object dstObj = nativeHeaders.get("destination");
    List<String> destinations = objectMapper.convertValue(dstObj,
        new TypeReference<>() {
        });

    for (String destination : destinations) {
      switch (destination) {
        case CHAT -> sendChat();
        case BEHAVIOR -> sendBehavior();
        case STAT -> sendStatistic();
      }
    }
  }

  public void sendChat() {
    List<ChatResponseDto> chats = messageService.getTodayChat();
    for (ChatResponseDto chat : chats) {
      messagePublisher.send(CHAT, chat);
    }
  }

  public void sendBehavior() {
    List<BehaviorResponseDto> behaviors = behaviorService.getTodayBehaviorChart();
    for (BehaviorResponseDto behavior : behaviors) {
      messagePublisher.send(BEHAVIOR, behavior);
    }
  }

  public void sendStatistic() {
    StatisticResponseDto response = statisticService.getStatistic(
        LocalDateTime.now().getMonthValue());
    messagePublisher.sendStatistic(response);
  }
}
