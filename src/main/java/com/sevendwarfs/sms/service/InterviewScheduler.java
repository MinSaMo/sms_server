package com.sevendwarfs.sms.service;

import java.time.LocalTime;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class InterviewScheduler {

  private final ThreadPoolTaskScheduler taskScheduler;

  public InterviewScheduler() {
    this.taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.initialize();
  }

  public void scheduleTask(Runnable runnable, LocalTime time) {
    taskScheduler.schedule(runnable, triggerContext -> {
      CronTrigger cronTrigger = new CronTrigger(generateCronExpression(time));
      return cronTrigger.nextExecution(triggerContext);
    });
  }

  private String generateCronExpression(LocalTime time) {
    int hour = time.getHour();
    int minute = time.getMinute();
    return String.format("0 %d %d ? * *", minute, hour);
  }
}
