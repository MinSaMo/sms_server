package com.sevendwarfs.sms.controller.stomp;

import com.sevendwarfs.sms.controller.stomp.dto.request.BehaviorRequestDto;
import com.sevendwarfs.sms.controller.stomp.dto.request.ChatRequestDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.BehaviorResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import com.sevendwarfs.sms.global.exception.GptRemoteServerError;
import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.ChatService;
import com.sevendwarfs.sms.service.MessageService;
import com.sevendwarfs.sms.service.StatisticService;
import com.sevendwarfs.sms.service.enums.MessageClassification;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DashboardController {

  private final MessagePublisher messagePublisher;
  private final ChatService chatService;
  private final MessageService messageService;
  private final BehaviorService behaviorService;
  private final StatisticService statisticService;

  private final String NOT_SUPPORTED = "죄송합니다, 저희는 알 수 없는 정보에 대해선 알려드릴 수 없습니다.";

  @MessageMapping("/chat")
  @SendTo("/topic/chat")
  public ChatResponseDto chat(
      ChatRequestDto dto
  ) {

    String script = dto.script();
    Long userMessageId = messageService.createUserMessage(script);
    messagePublisher.sendMessage(userMessageId, script, ChatResponseDto.USER);

    try {

      MessageClassification classification = chatService.classifyMessage(script);
      messagePublisher.sendClassification(userMessageId, script, classification.getVal());

      Long replyId;
      if (classification.equals(MessageClassification.NOT_SUPPORTED)) {
        replyId = messageService.createAssistantMessage(NOT_SUPPORTED);
        startBackgroundJob(() -> {
          Optional<Long> isOdd = chatService.recognizeMessage(userMessageId);
          if (isOdd.isPresent()) {
            Long id = isOdd.get();
            messagePublisher.sendMessageModified(id,true);
            StatisticResponseDto statistic = statisticService.getStatistic(
                LocalDateTime.now().getMonthValue());
            messagePublisher.sendStatistic(statistic);
          }
        });
        return ChatResponseDto.builder()
            .id(replyId)
            .sender(ChatResponseDto.ASSISTANT)
            .isOdd(true)
            .script(NOT_SUPPORTED)
            .build();
      }

      String reply = chatService.replyToMessage(script);
      replyId = messageService.createAssistantMessage(reply);

      startBackgroundJob(() -> {
        Optional<Long> isOdd = chatService.recognizeMessage(userMessageId);
        if (isOdd.isPresent()) {
          Long id = isOdd.get();
          messagePublisher.sendMessageModified(id,true);
          StatisticResponseDto statistic = statisticService.getStatistic(
              LocalDateTime.now().getMonthValue());
          messagePublisher.sendStatistic(statistic);
        }
      });

      return ChatResponseDto.builder()
          .script(reply)
          .id(replyId)
          .sender(ChatResponseDto.ASSISTANT)
          .isOdd(false)
          .build();
    } catch (GptRemoteServerError error) {
      return ChatResponseDto.builder()
          .id(-1L)
          .script(error.getMessage())
          .sender(ChatResponseDto.ASSISTANT)
          .isOdd(true)
          .build();
    }

  }

  @MessageMapping("/caption")
  @SendTo("/topic/behavior")
  public BehaviorResponseDto analyzeBehavior(
      BehaviorRequestDto dto
  ) {
    String caption = dto.caption();
    Long videoId = dto.videoId();
    try {

      boolean isOdd = behaviorService.recognitionBehavior(caption, videoId);
      if (isOdd) {
        startBackgroundJob(() -> {
          StatisticResponseDto statistic = statisticService.getStatistic(
              LocalDateTime.now().getMonthValue());
          messagePublisher.sendStatistic(statistic);
        });
      }
      int sec = new Random().nextInt(5) + 2;
      Thread.sleep(sec * 1000);
      return BehaviorResponseDto.builder()
          .caption(caption)
          .isOdd(isOdd)
          .build();
    } catch (GptRemoteServerError error) {
      return BehaviorResponseDto.builder()
          .caption(error.getMessage())
          .isOdd(false)
          .build();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void startBackgroundJob(Runnable runnable) {
    new Thread(runnable).start();
  }
}
