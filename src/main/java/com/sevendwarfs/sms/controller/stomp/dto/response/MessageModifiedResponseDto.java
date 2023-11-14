package com.sevendwarfs.sms.controller.stomp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageModifiedResponseDto {

  private Long id;
  private boolean isOdd;

}
