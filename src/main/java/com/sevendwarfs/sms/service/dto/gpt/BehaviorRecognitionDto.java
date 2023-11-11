package com.sevendwarfs.sms.service.dto.gpt;

public record BehaviorRecognitionDto(
    Boolean isDetected,
    String reason
) {
}
