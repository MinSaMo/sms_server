package com.sevendwarfs.sms.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageClassification {
  SUPPORTED(0),
  NOT_SUPPORTED(1),;
  @Getter
  private final int val;

  public static MessageClassification of(int i) {
    for (MessageClassification m : MessageClassification.values()) {
      if (m.getVal() == i) {
        return m;
      }
    }
    String msg = String.format("No such classification : %s", i);
    throw new IllegalArgumentException(msg);
  }
  @Override
  public String toString() {
    return name();
  }
}
