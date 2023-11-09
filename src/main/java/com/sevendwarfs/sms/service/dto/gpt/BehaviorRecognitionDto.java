package com.sevendwarfs.sms.service.dto.gpt;

public record BehaviorRecognitionDto(
    Boolean isDetected,
    String detectedBehavior,
    String explanation,
    String evidence
) {

  @Override
  public String toString() {
    return "BehaviorRecognitionDto{" +
        "isDetected=" + isDetected +
        ", detectedBehavior='" + detectedBehavior + '\'' +
        ", explanation='" + explanation + '\'' +
        ", evidence='" + evidence + '\'' +
        '}';
  }
}
