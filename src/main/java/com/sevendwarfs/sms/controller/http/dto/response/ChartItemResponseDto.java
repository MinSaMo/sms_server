package com.sevendwarfs.sms.controller.http.dto.response;

import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewLog;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddMessage;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChartItemResponseDto {

  private String type;
  private String content;
  private LocalDateTime timestamp;
  private static final String MESSAGE = "이상발화";
  private static final String BEHAVIOR = "이상행동";
  private static final String INTERVIEW = "질문";

  @Builder
  public ChartItemResponseDto(String type, String content, LocalDateTime timestamp) {
    this.type = type;
    this.content = content;
    this.timestamp = timestamp;
  }

  public static ChartItemResponseDto mock(String type, String content) {
    return ChartItemResponseDto.builder()
        .type(type)
        .content(content)
        .timestamp(LocalDateTime.now())
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ChartItemResponseDto target)) {
      return false;
    }
    // Compare content and type
    boolean contentEquals = this.content.equals(target.content);
    boolean typeEquals = this.type.equals(target.type);

    // Compare timestamp up to minutes
    boolean timestampEquals = this.timestamp.getYear() == target.timestamp.getYear()
        && this.timestamp.getMonth() == target.timestamp.getMonth()
        && this.timestamp.getDayOfMonth() == target.timestamp.getDayOfMonth()
        && this.timestamp.getHour() == target.timestamp.getHour()
        && this.timestamp.getMinute() == target.timestamp.getMinute();

    return contentEquals && typeEquals && timestampEquals;
  }

  public static ChartItemResponseDto of(OddMessage oddMessage, Message message) {
    StringBuilder sb = new StringBuilder();
    if (oddMessage.getIsDisorganized()) {
      sb.append("와해된 언어 ");
    }
    if (oddMessage.getIsDelusions()) {
      sb.append("환각 ");
    }
    if (oddMessage.getIsLinguisticDerailment()) {
      sb.append("언어의 탈선 ");
    }
    if (oddMessage.getIsHallucination()) {
      sb.append("망상 ");
    }
    sb.append("증상");
    return ChartItemResponseDto.builder()
        .type(MESSAGE)
        .content(sb.toString())
        .timestamp(message.getTimestamp())
        .build();
  }

  public static ChartItemResponseDto of(OddBehavior oddBehavior, Behavior behavior) {
    String content = "기이행동 증상";
    return ChartItemResponseDto.builder()
        .type(BEHAVIOR)
        .content(content)
        .timestamp(behavior.getTimestamp())
        .build();
  }

  public static ChartItemResponseDto of(InterviewLog log, Interview interview) {
    return ChartItemResponseDto.builder()
        .type(INTERVIEW)
        .content(interview.getQuestion())
        .timestamp(log.getTimestamp())
        .build();
  }

}
