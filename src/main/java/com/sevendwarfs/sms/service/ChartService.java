package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.response.ChartItemResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddItemResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewLog;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChartService {

  private final MessageService messageService;
  private final BehaviorService behaviorService;
  private final InterviewService interviewService;

  @Value("${video.base}")
  private String videoBaseURL;

  @Transactional
  public ChartResponseDto generateChart() {
    List<OddMessage> oddMessages = messageService.findTodayOddMessage();
    List<OddBehavior> oddBehaviors = behaviorService.findTodayOddBehavior();
    List<InterviewLog> interviewLogs = interviewService.findTodayLog();

    List<ChartItemResponseDto> messageList = oddMessages.stream()
        .map(oddMessage -> {
          Message message = oddMessage.getMessage();
          return ChartItemResponseDto.of(oddMessage, message);
        })
        .toList();

    List<ChartItemResponseDto> behaviorList = oddBehaviors.stream()
        .map(oddBehavior -> {
          Behavior behavior = oddBehavior.getBehavior();
          return ChartItemResponseDto.of(oddBehavior, behavior);
        })
        .toList();

    List<ChartItemResponseDto> interviewList = interviewLogs.stream()
        .map(log -> {
          Interview interview = log.getInterview();
          return ChartItemResponseDto.of(log, interview);
        })
        .toList();

    List<ChartItemResponseDto> combinedList = Stream.concat(
            Stream.concat(messageList.stream(), behaviorList.stream()),
            interviewList.stream())
        .toList();
    List<ChartItemResponseDto> result = new ArrayList<>(combinedList);
    result.sort(Comparator.comparing(ChartItemResponseDto::getTimestamp));
    return ChartResponseDto.builder()
        .logs(result)
        .build();
  }

  @Transactional
  public OddListResponseDto generateOddList() {
    List<OddMessage> oddMessages = messageService.findOddMessageWeekly();
    List<OddBehavior> oddBehaviors = behaviorService.findOddBehaviorWeekly();

    List<OddItemResponseDto> messageList = oddMessages.stream()
        .map(oddMsg -> {
          Message message = oddMsg.getMessage();
          return OddItemResponseDto.of(oddMsg, message, message.getDialog().getId());
        })
        .toList();

    List<OddItemResponseDto> behaviorList = oddBehaviors.stream()
        .map(oddBehavior -> {
          Behavior behavior = oddBehavior.getBehavior();
          return OddItemResponseDto.of(oddBehavior, behavior);
        })
        .toList();

    List<OddItemResponseDto> combinedList = new java.util.ArrayList<>(
        Stream.concat(messageList.stream(),
                behaviorList.stream())
            .toList());
    List<OddItemResponseDto> result = new ArrayList<>(combinedList);
    result.sort(Comparator.comparing(OddItemResponseDto::getTimestamp));
    return OddListResponseDto.builder()
        .logs(combinedList)
        .build();
  }

  @Transactional
  public Workbook makeXLSXFile() {
    String fileName = LocalDateTime.now().toLocalDate().toString();
    List<OddMessage> oddMessages = messageService.findTodayOddMessage();
    List<OddBehavior> oddBehaviors = behaviorService.findTodayOddBehavior();
    List<InterviewLog> interviewLogs = interviewService.findTodayLog();
    try (Workbook workbook = new HSSFWorkbook()) {
      Sheet sheet = workbook.createSheet(fileName);
      int colSize = 5;

      int objCount = oddMessages.size() + oddBehaviors.size() + interviewLogs.size();
      int messageIndex, behaviorIndex, interviewIndex;

      messageIndex = behaviorIndex = interviewIndex = 0;

      int idx = 1;
      while (idx <= objCount) {
        OddMessage oddMessage = null;
        OddBehavior oddBehavior = null;
        InterviewLog interviewLog = null;
        if (messageIndex < oddMessages.size()) {
          oddMessage = oddMessages.get(messageIndex);
        }
        if (behaviorIndex < oddBehaviors.size()) {
          oddBehavior = oddBehaviors.get(behaviorIndex);
        }
        if (interviewIndex < interviewLogs.size()) {
          interviewLog = interviewLogs.get(interviewIndex);
        }

        int first = findFirst(oddMessage, oddBehavior, interviewLog);
        CellStyle headStyle = createHeadStyle(workbook);
        CellStyle oddStyle = createOddStyle(workbook);
        switch (first) {
          case 1 -> {
            createMessageSection(sheet, headStyle, oddStyle, idx++, oddMessage);
            messageIndex++;
          }
          case 2 -> {
            createBehaviorSection(sheet, headStyle, idx++, oddBehavior);
            behaviorIndex++;
          }
          case 3 -> {
            createInterviewSection(sheet, headStyle, idx++, interviewLog);
            interviewIndex++;
          }
        }
      }

      for (int i = 0; i < colSize; i++) {
        sheet.autoSizeColumn(i);
      }
      return workbook;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  public int findFirst(OddMessage oddMessage, OddBehavior oddBehavior, InterviewLog interviewLog) {
    int res = 0;

    if (oddMessage != null && oddBehavior != null && interviewLog != null) {
      // Case 1: All parameters are not null
      LocalDateTime messageTime = oddMessage.getMessage().getTimestamp();
      LocalDateTime behaviorTime = oddBehavior.getBehavior().getTimestamp();
      LocalDateTime interviewTime = interviewLog.getTimestamp();

      if (messageTime.isBefore(behaviorTime) && messageTime.isBefore(interviewTime)) {
        res = 1;
      } else if (behaviorTime.isBefore(messageTime) && behaviorTime.isBefore(interviewTime)) {
        res = 2;
      } else if (interviewTime.isBefore(messageTime) && interviewTime.isBefore(behaviorTime)) {
        res = 3;
      }
    } else if (oddMessage != null && oddBehavior != null) {
      // Case 2: oddMessage is not null, oddBehavior is not null, interviewLog is null
      LocalDateTime messageTime = oddMessage.getMessage().getTimestamp();
      LocalDateTime behaviorTime = oddBehavior.getBehavior().getTimestamp();

      if (messageTime.isBefore(behaviorTime)) {
        res = 1;
      } else {
        res = 2;
      }
    } else if (oddMessage != null && interviewLog != null) {
      // Case 3: oddMessage is not null, oddBehavior is null, interviewLog is not null
      LocalDateTime messageTime = oddMessage.getMessage().getTimestamp();
      LocalDateTime interviewTime = interviewLog.getTimestamp();

      if (messageTime.isBefore(interviewTime)) {
        res = 1;
      } else {
        res = 3;
      }
    } else if (oddBehavior != null && interviewLog != null) {
      // Case 4: oddMessage is null, oddBehavior is not null, interviewLog is not null
      LocalDateTime behaviorTime = oddBehavior.getBehavior().getTimestamp();
      LocalDateTime interviewTime = interviewLog.getTimestamp();

      if (behaviorTime.isBefore(interviewTime)) {
        res = 2;
      } else {
        res = 3;
      }
    } else if (oddMessage != null) {
      // Case 5: oddMessage is not null, oddBehavior is null, interviewLog is null
      res = 1;
    } else if (oddBehavior != null) {
      // Case 6: oddMessage is null, oddBehavior is not null, interviewLog is null
      res = 2;
    } else if (interviewLog != null) {
      // Case 7: oddMessage is null, oddBehavior is null, interviewLog is not null
      res = 3;
    }

    return res;
  }

  @Transactional
  public void createMessageSection(Sheet sheet, CellStyle style, CellStyle oddStyle, int idx,
      OddMessage oddMessage) {
    Cell cell;
    Row row;
    Map<Integer, String> headValueMap = getMessageHeaderValueMap(idx, oddMessage);

    row = sheet.createRow(sheet.getLastRowNum() + 1);
    for (int i = 0; i < 5; i++) {
      cell = row.createCell(i);
      cell.setCellValue(headValueMap.get(i));
      cell.setCellStyle(style);
    }

    Map<Integer, String> bodyValueMap;
    List<Message> messageList = oddMessage.getMessage().getDialog().getMessageList();
    for (Message message : messageList) {
      bodyValueMap = getMessageBodyValueMap(message);
      row = sheet.createRow(sheet.getLastRowNum() + 1);
      for (int i = 1; i <= 3; i++) {
        cell = row.createCell(i);
        cell.setCellValue(bodyValueMap.get(i));
        if (message.equals(oddMessage.getMessage())) {
          cell.setCellStyle(oddStyle);
        }
      }
    }
  }

  private Map<Integer, String> getMessageBodyValueMap(Message message) {
    Map<Integer, String> bodyValueMap = new HashMap<>();
    bodyValueMap.put(1, message.getTimestamp().toString());
    bodyValueMap.put(2, message.getSender());
    bodyValueMap.put(3, message.getContent());
    return bodyValueMap;
  }

  private static Map<Integer, String> getMessageHeaderValueMap(int idx, OddMessage oddMessage) {
    final String TYPE = "발화";
    Map<Integer, String> headValueMap = new HashMap<>();
    headValueMap.put(0, String.valueOf(idx));
    headValueMap.put(1, oddMessage.getMessage().getTimestamp().toString());
    headValueMap.put(2, TYPE);
    headValueMap.put(3, oddMessage.symptomString());
    headValueMap.put(4, oddMessage.getReason());
    return headValueMap;
  }

  @Transactional
  public void createBehaviorSection(Sheet sheet, CellStyle style, int idx,
      OddBehavior oddBehavior) {

    Cell cell;
    Row row;
    Map<Integer, String> headValueMap = getBehaviorHeaderValueMap(idx, oddBehavior);
    row = sheet.createRow(sheet.getLastRowNum() + 1);
    for (int i = 0; i < 5; i++) {
      cell = row.createCell(i);
      cell.setCellValue(headValueMap.get(i));
      cell.setCellStyle(style);
    }

    row = sheet.createRow(sheet.getLastRowNum() + 1);
    cell = row.createCell(1);
    Behavior behavior = oddBehavior.getBehavior();
    cell.setCellValue(behavior.getTimestamp().toString());
    cell = row.createCell(2);
    cell.setCellValue(behavior.getCaption());
    cell = row.createCell(3);
    String url = String.format("%s%d", videoBaseURL, oddBehavior.getVideoId());
    cell.setCellValue(url);
    cell = row.createCell(4);
    cell.setCellValue(oddBehavior.getReason());
  }

  private Map<Integer, String> getBehaviorHeaderValueMap(int idx, OddBehavior oddBehavior) {
    final String TYPE = "행동";
    final String SYMPTOM = "기이 행동";
    Map<Integer, String> headValueMap = new HashMap<>();
    headValueMap.put(0, String.valueOf(idx));
    headValueMap.put(1, oddBehavior.getBehavior().getTimestamp().toString());
    headValueMap.put(2, TYPE);
    headValueMap.put(3, SYMPTOM);
    headValueMap.put(4, oddBehavior.getReason());
    return headValueMap;
  }

  @Transactional
  public void createInterviewSection(Sheet sheet, CellStyle style, int idx,
      InterviewLog interviewLog) {

    Cell cell;
    Row row;
    Interview interview = interviewLog.getInterview();
    Map<Integer, String> headValueMap = new HashMap<>();
    headValueMap.put(0, String.valueOf(idx));
    headValueMap.put(1, interviewLog.getTimestamp().toString());
    headValueMap.put(2, "질문");
    headValueMap.put(3, interview.getQuestion());

    row = sheet.createRow(sheet.getLastRowNum() + 1);
    for (int i = 0; i < 5; i++) {
      cell = row.createCell(i);
      cell.setCellValue(headValueMap.get(i));
      cell.setCellStyle(style);
    }

    Map<Integer, String> bodyValueMap;
    List<Message> messageList = interviewLog.getDialog().getMessageList();
    for (Message message : messageList) {
      bodyValueMap = getMessageBodyValueMap(message);
      row = sheet.createRow(sheet.getLastRowNum() + 1);
      for (int i = 1; i <= 3; i++) {
        cell = row.createCell(i);
        cell.setCellValue(bodyValueMap.get(i));
      }
    }
  }

  private CellStyle createHeadStyle(Workbook workbook) {
    CellStyle headStyle = workbook.createCellStyle();
    headStyle.setBorderTop(BorderStyle.THIN);
    headStyle.setBorderBottom(BorderStyle.THIN);
    headStyle.setBorderLeft(BorderStyle.THIN);
    headStyle.setBorderRight(BorderStyle.THIN);
    headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
    headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headStyle.setAlignment(HorizontalAlignment.CENTER);
    return headStyle;
  }

  private CellStyle createOddStyle(Workbook workbook) {
    CellStyle oddStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setColor(Font.COLOR_RED);
    oddStyle.setFont(font);
    return oddStyle;
  }
}
