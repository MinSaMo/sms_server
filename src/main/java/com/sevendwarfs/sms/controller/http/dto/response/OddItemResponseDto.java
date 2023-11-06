package com.sevendwarfs.sms.controller.http.dto.response;

import com.sevendwarfs.sms.domain.Behavior;
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
public class OddItemResponseDto {

  private Long id;
  private String type;
  private LocalDateTime timestamp;
  private String content;
  private String reason;
  private Long reference;

  private static final String TYPE_MESSAGE = "이상발화";
  private static final String TYPE_BEHAVIOR = "이상행동";

  @Builder
  public OddItemResponseDto(Long id, String type, LocalDateTime timestamp, String content,
      String reason, Long reference) {
    this.id = id;
    this.type = type;
    this.timestamp = timestamp;
    this.content = content;
    this.reason = reason;
    this.reference = reference;
  }

  public static OddItemResponseDto of(OddMessage obj, Message message, Long dialogId) {
    return OddItemResponseDto.builder()
        .id(obj.getId())
        .type(TYPE_MESSAGE)
        .timestamp(message.getTimestamp())
        .content(message.getContent())
        .reason(obj.getReason())
        .reference(dialogId)
        .build();
  }

  public static OddItemResponseDto of(OddBehavior obj, Behavior behavior) {
    return OddItemResponseDto.builder()
        .id(obj.getId())
        .type(TYPE_BEHAVIOR)
        .timestamp(behavior.getTimestamp())
        .content(behavior.getCaption())
        .reason(obj.getReason())
        .reference(obj.getVideoId())
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof OddItemResponseDto target)) {
      return false;
    }
    // Compare content and type
    boolean contentEquals = this.content.equals(target.content)
        && this.reason.equals(target.reason)
        && this.reference.equals(target.reference);
    boolean typeEquals = this.type.equals(target.type);

    // Compare timestamp up to minutes
    boolean timestampEquals = this.timestamp.getYear() == target.timestamp.getYear()
        && this.timestamp.getMonth() == target.timestamp.getMonth()
        && this.timestamp.getDayOfMonth() == target.timestamp.getDayOfMonth()
        && this.timestamp.getHour() == target.timestamp.getHour()
        && this.timestamp.getMinute() == target.timestamp.getMinute();

    return contentEquals && typeEquals && timestampEquals;
  }
}
