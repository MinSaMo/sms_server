package com.sevendwarfs.sms.controller.stomp;

import com.sevendwarfs.sms.controller.stomp.dto.request.BehaviorRequestDto;
import com.sevendwarfs.sms.controller.stomp.dto.request.ChatRequestDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.BehaviorResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DashboardController {

  private final MessagePublisher messagePublisher;

  @MessageMapping("/chat")
  @SendTo("/topic/chat")
  public ChatResponseDto chat(
      ChatRequestDto dto
  ) {
    messagePublisher.statisticMock();
    return ChatResponseDto.mock(dto.script());
  }

  @MessageMapping("/caption")
  @SendTo("/topic/behavior")
  public BehaviorResponseDto analyzeBehavior(
      BehaviorRequestDto dto
  ) {
    messagePublisher.statisticMock();
    return BehaviorResponseDto.mock(dto.caption());
  }

}
