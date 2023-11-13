package com.sevendwarfs.sms.config;

import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.DialogRepository;
import com.sevendwarfs.sms.domain.InterviewLogRepository;
import com.sevendwarfs.sms.domain.InterviewRepository;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.DialogService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfigure {

  private final BehaviorRepository behaviorRepository;
  private final OddBehaviorRepository oddBehaviorRepository;

  private final MessageRepository messageRepository;
  private final OddMessageRepository oddMessageRepository;

  private final DialogRepository dialogRepository;
  private final InterviewRepository interviewRepository;
  private final InterviewLogRepository interviewLogRepository;

  private final DialogService dialogService;

  @PostConstruct
  @Transactional
  public void init() {
    deleteEntity();
    createMockData();
    dialogService.init();
  }

  @Transactional
  public void createMockData() {

  }

  @Transactional
  public void deleteEntity() {
    log.info("Delete odd message");
    oddMessageRepository.deleteAll();
    log.info("Delete odd behavior");
    oddBehaviorRepository.deleteAll();

    log.info("Delete interview log");
    interviewLogRepository.deleteAll();
    log.info("Delete interview");
    interviewRepository.deleteAll();

    log.info("Delete behavior");
    behaviorRepository.deleteAll();
    log.info("Delete Message");
    messageRepository.deleteAll();
    log.info("Delete Dialog");
    dialogRepository.deleteAll();
  }
}
