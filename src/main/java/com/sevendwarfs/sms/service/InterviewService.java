package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import com.sevendwarfs.sms.controller.stomp.MessagePublisher;
import com.sevendwarfs.sms.controller.stomp.dto.response.ChatResponseDto;
import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewLog;
import com.sevendwarfs.sms.domain.InterviewLogRepository;
import com.sevendwarfs.sms.domain.InterviewRepository;
import com.sevendwarfs.sms.service.dto.gpt.NurseInterviewResponseDto;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewService {

  private final InterviewRepository interviewRepository;
  private final InterviewLogRepository interviewLogRepository;
  private final GptService gptService;
  private final PromptManager promptManager;
  private final InterviewScheduler scheduler;
  private final MessagePublisher messagePublisher;
  private final MessageService messageService;
  private final DialogService dialogService;

  @Transactional
  public InterviewResponseDto createInterview(InterviewCreateDto dto) {
    Interview interview = interviewRepository.save(Interview.builder()
        .question(dto.question())
        .questionTime(dto.time())
        .build());
    return InterviewResponseDto.of(interview);
  }

  @Transactional
  public void scheduleInterview(Long interviewId) {
    Interview interview = findById(interviewId);
    scheduler.scheduleTask(() -> {
      String question = generateInterviewScript(interviewId);
      Long msgId = messageService.createAssistantMessage(question);
      Dialog dialog = dialogService.getCurrentDialog();
      messagePublisher.sendMessage(msgId, question, ChatResponseDto.ASSISTANT);
      saveInterviewLog(interview,dialog);
    },interview.getQuestionTime());
  }

  @Transactional
  public List<InterviewResponseDto> findAll() {
    return interviewRepository.findAll().stream()
        .map(InterviewResponseDto::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public void saveInterviewLog(Interview interview, Dialog dialog) {
    interviewLogRepository.save(InterviewLog.builder()
        .interview(interview)
        .dialog(dialog)
        .build());
  }

  @Transactional
  public String generateInterviewScript(Long interviewId) {
    Interview interview = findById(interviewId);
    Prompt prompt = promptManager.getNurseInterview();
    ChatCompletionRequest request = gptService.request()
        .addSystemMessage(prompt.getScript())
        .addUserMessage(interview.getQuestion())
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();

    NurseInterviewResponseDto response = gptService.askToSub(request, NurseInterviewResponseDto.class);
    return response.script();
  }

  @Transactional
  public Interview findById(Long interviewId) {
    return interviewRepository.findById(interviewId)
        .orElseThrow(() -> new RuntimeException("Not found Interview"));
  }

  @Transactional
  public List<InterviewLog> findTodayLog() {
    LocalDateTime start = startOfDay();
    LocalDateTime end = endOfDay();
    return interviewLogRepository.findByTimestampBetween(start, end).stream()
        .sorted((l1, l2) -> l1.getTimestamp()
            .isAfter(l2.getTimestamp()) ? 1 : -1)
        .toList();
  }

  protected LocalDateTime startOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
  }

  protected LocalDateTime endOfDay() {
    LocalDate now = LocalDate.now();
    return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
  }
}
