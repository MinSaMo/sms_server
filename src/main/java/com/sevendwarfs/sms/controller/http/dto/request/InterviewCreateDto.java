package com.sevendwarfs.sms.controller.http.dto.request;

import java.time.LocalDateTime;

public record InterviewCreateDto(
    String question,
    LocalDateTime time
) {}
