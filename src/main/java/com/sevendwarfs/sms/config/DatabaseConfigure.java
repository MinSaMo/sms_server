package com.sevendwarfs.sms.config;

import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfigure {

  private final MessageService messageService;
  private final BehaviorService behaviorService;

  @PostConstruct
  private void init() {
  }
}
