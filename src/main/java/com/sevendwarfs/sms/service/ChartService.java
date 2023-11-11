package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.response.ChartItemResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddItemResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewLog;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddMessage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChartService {

  private final MessageService messageService;
  private final BehaviorService behaviorService;
  private final InterviewService interviewService;

  @Transactional
  public ChartResponseDto generateChart() {
    List<OddMessage> oddMessages = messageService.findTodayOddMessage();
    List<OddBehavior> oddBehaviors = behaviorService.findTodayOddBehavior();
    List<InterviewLog> interviewLogs = interviewService.findTodayLog();

    List<ChartItemResponseDto> messageList = oddMessages.stream()
        .map(oddMessage -> {
          Message message = oddMessage.getMessage();
          return ChartItemResponseDto.of(oddMessage, message);
        })
        .toList();

    List<ChartItemResponseDto> behaviorList = oddBehaviors.stream()
        .map(oddBehavior -> {
          Behavior behavior = oddBehavior.getBehavior();
          return ChartItemResponseDto.of(oddBehavior, behavior);
        })
        .toList();

    List<ChartItemResponseDto> interviewList = interviewLogs.stream()
        .map(log -> {
          Interview interview = log.getInterview();
          return ChartItemResponseDto.of(log, interview);
        })
        .toList();

    List<ChartItemResponseDto> combinedList = Stream.concat(
            Stream.concat(messageList.stream(), behaviorList.stream()),
            interviewList.stream())
        .toList();
    List<ChartItemResponseDto> result = new ArrayList<>(combinedList);
    result.sort(Comparator.comparing(ChartItemResponseDto::getTimestamp));
    return ChartResponseDto.builder()
        .logs(result)
        .build();
  }

  @Transactional
  public OddListResponseDto generateOddList() {
    List<OddMessage> oddMessages = messageService.findAllOddMessage();
    List<OddBehavior> oddBehaviors = behaviorService.findAllOddBehavior();

    List<OddItemResponseDto> messageList = oddMessages.stream()
        .map(oddMsg -> {
          Message message = oddMsg.getMessage();
          return OddItemResponseDto.of(oddMsg, message, message.getDialog().getId());
        })
        .toList();

    List<OddItemResponseDto> behaviorList = oddBehaviors.stream()
        .map(oddBehavior -> {
          Behavior behavior = oddBehavior.getBehavior();
          return OddItemResponseDto.of(oddBehavior, behavior);
        })
        .toList();

    List<OddItemResponseDto> combinedList = new java.util.ArrayList<>(
        Stream.concat(messageList.stream(),
                behaviorList.stream())
            .toList());
    List<OddItemResponseDto> result = new ArrayList<>(combinedList);
    result.sort(Comparator.comparing(OddItemResponseDto::getTimestamp));
    return OddListResponseDto.builder()
        .logs(combinedList)
        .build();
  }
}
