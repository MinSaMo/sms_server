package com.sevendwarfs.sms.controller.http.dto.request;

import java.time.LocalTime;

public record InterviewCreateDto(
    String question,
    LocalTime time
) {}
