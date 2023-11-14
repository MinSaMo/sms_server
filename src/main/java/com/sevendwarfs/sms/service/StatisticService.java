package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.stomp.dto.response.StatisticResponseDto;
import com.sevendwarfs.sms.controller.stomp.dto.response.WeeklyStatisticResponseDto;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {

  private final OddMessageRepository oddMessageRepository;
  private final OddBehaviorRepository oddBehaviorRepository;

  @Transactional
  public StatisticResponseDto getStatistic(int month) {

    LocalDateTime startOfMonth = startOfMonth(month);
    LocalDateTime endOfMonth = endOfMonth(month);

    Map<Integer, List<OddMessage>> messageMap =
        oddMessageRepository
            .findByMessageTimestampBetween(startOfMonth, endOfMonth)
            .stream()
            .collect(
                Collectors.groupingBy(message ->
                    message.getMessage().getTimestamp().get(WeekFields.ISO.weekOfMonth())));

    Map<Integer, List<OddBehavior>> behaviorMap =
        oddBehaviorRepository
            .findByBehaviorTimestampBetween(startOfMonth, endOfMonth)
            .stream()
            .collect(
                Collectors.groupingBy(behavior ->
                    behavior.getBehavior().getTimestamp().get(WeekFields.ISO.weekOfMonth())));

    addEmptyWeeks(messageMap, LocalDateTime.now());
    addEmptyWeeks(behaviorMap, LocalDateTime.now());

    return StatisticResponseDto.builder()
        .month(month)
        .weeklyData(getWeeklyStatistic(messageMap, behaviorMap))
        .build();
  }

  public int getMaxWeekOfMonth(LocalDateTime dateTime) {
    Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.YEAR, dateTime.getYear());
    calendar.set(Calendar.MONTH, dateTime.getMonthValue() - 1);

    return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
  }

  private <T> void addEmptyWeeks(Map<Integer, List<T>> map, LocalDateTime time) {
    int maxWeekOfMonth = getMaxWeekOfMonth(time);
    for (int week = 1; week <= maxWeekOfMonth; week++) {
      map.putIfAbsent(week, Collections.emptyList());
    }
  }

  @Transactional
  public Map<Integer, WeeklyStatisticResponseDto> getWeeklyStatistic(
      Map<Integer, List<OddMessage>> messages, Map<Integer, List<OddBehavior>> behaviors) {
    Map<Integer, WeeklyStatisticResponseDto> result = new HashMap<>();
    int behaviorCount, hwangakCount, mangsangCount, wahaeCount, talsunCount;
    for (Integer week : messages.keySet()) {
      hwangakCount = mangsangCount = wahaeCount = talsunCount = 0;
      behaviorCount = behaviors.get(week).size();
      List<OddMessage> oddMessages = messages.get(week);
      for (OddMessage oddMessage : oddMessages) {
        if (oddMessage.getIsLinguisticDerailment()) {
          talsunCount++;
        }
        if (oddMessage.getIsDelusions()) {
          hwangakCount++;
        }
        if (oddMessage.getIsHallucination()) {
          mangsangCount++;
        }
        if (oddMessage.getIsDisorganized()) {
          wahaeCount++;
        }
      }
      result.put(week, WeeklyStatisticResponseDto.builder()
          .keeyee(behaviorCount)
          .talsun(talsunCount)
          .wahae(wahaeCount)
          .mangsang(mangsangCount)
          .hwangak(hwangakCount)
          .build());
    }
    return result;
  }

  protected LocalDateTime startOfMonth(int month) {
    return LocalDateTime.of(LocalDate.now().getYear(), month, 1, 0, 0, 0);
  }

  protected LocalDateTime endOfMonth(int month) {
    return this.startOfMonth(month)
        .plusMonths(1).withDayOfMonth(1).minusDays(1).with(LocalTime.MAX);
  }
}
