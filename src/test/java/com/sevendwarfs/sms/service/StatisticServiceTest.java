package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.WeeklyStatisticResponseDto;
import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class StatisticServiceTest {


  @Autowired
  StatisticService statisticService;

  @Autowired
  MessageService messageService;

  @Autowired
  BehaviorService behaviorService;

  int[] lingCount = {0, 0, 0, 0, 0, 0};
  int[] delCount = {0, 0, 0, 0, 0, 0};
  int[] halCount = {0, 0, 0, 0, 0, 0};
  int[] disCount = {0, 0, 0, 0, 0, 0};
  int[] behaviorCount = {0, 0, 0, 0, 0, 0};

  @Test
  @Transactional
  void 통계_테스트() {
    int T;
    T = new Random().nextInt(16) + 5;
    for (int i = 0; i < T; i++) {
      createRandomMessage();
      createRandomBehavior();
    }

    int monthValue = LocalDateTime.now().getMonthValue();
    StatisticResponseDto result = statisticService.getStatistic(
        monthValue);
    assertEquals(result.getMonth(), monthValue);
    Map<Integer, WeeklyStatisticResponseDto> weeklyData = result.getWeeklyData();
    for (Integer i : weeklyData.keySet()) {
      WeeklyStatisticResponseDto data = weeklyData.get(i);
      assertEquals(lingCount[i],data.getTalsun());
      assertEquals(delCount[i], data.getHwangak());
      assertEquals(halCount[i], data.getMangsang());
      assertEquals(disCount[i], data.getWahae());
      assertEquals(behaviorCount[i], data.getKeeyee());
    }
  }

  @Transactional
  void createRandomMessage() {
    boolean lin, del, hal, dis;
    lin = Math.random() >= 0.5;
    del = Math.random() >= 0.5;
    hal = Math.random() >= 0.5;
    dis = Math.random() >= 0.5;
    Long msgId = messageService.createUserMessage("test");
    Message message = messageService.findById(msgId);
    LocalDateTime timestamp = message.getTimestamp();
    int weekOfMonth = getRandomWeekOfMonth(timestamp.getYear(), timestamp.getMonthValue());
    message.setTimestamp(
        timestamp.with(WeekFields.ISO.weekOfMonth(),
            weekOfMonth));
    // month 변경
    messageService.createOddMessage(msgId, new MessageRecognitionDto(lin, del, hal, dis, "test"));

    if (lin) {
      lingCount[weekOfMonth]++;
    }
    if (del) {
      delCount[weekOfMonth]++;
    }
    if (hal) {
      halCount[weekOfMonth]++;
    }
    if (dis) {
      disCount[weekOfMonth]++;
    }
  }

  @Transactional
  void createRandomBehavior() {
    Behavior behavior = behaviorService.createBehavior("test");

    // month 변경
    LocalDateTime timestamp = behavior.getTimestamp();
    int weekOfMonth = getRandomWeekOfMonth(timestamp.getYear(), timestamp.getMonthValue());
    behavior.setTimestamp(
        timestamp.with(WeekFields.ISO.weekOfMonth(),
            weekOfMonth));

    behaviorService.createOddBehavior(behavior, "test", 1L);
    behaviorCount[weekOfMonth]++;
  }

  public int getRandomWeekOfMonth(int year, int month) {
    Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1); // Calendar의 월은 0부터 시작하므로 1을 빼줍니다

    int maxWeeksInMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

    Random random = new Random();

    int randomWeek = random.nextInt(maxWeeksInMonth) + 1;

    return randomWeek;
  }
}