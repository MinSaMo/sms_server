package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.controller.http.dto.request.InterviewCreateDto;
import com.sevendwarfs.sms.controller.http.dto.response.InterviewResponseDto;
import com.sevendwarfs.sms.controller.stomp.MessagePublisher;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewRepository;
import com.sevendwarfs.sms.service.dto.gpt.NurseInterviewResponseDto;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
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
  private final GptService gptService;
  private final PromptManager promptManager;
  private final InterviewScheduler scheduler;
  private final MessagePublisher messagePublisher;

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
      messagePublisher.sendMessage(question);
    },interview.getQuestionTime());
  }

  @Transactional
  public List<InterviewResponseDto> findAll() {
    return interviewRepository.findAll().stream()
        .map(InterviewResponseDto::of)
        .collect(Collectors.toList());
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

    NurseInterviewResponseDto response = gptService.ask(request, NurseInterviewResponseDto.class);
    return response.script();
  }

  @Transactional
  public Interview findById(Long interviewId) {
    return interviewRepository.findById(interviewId)
        .orElseThrow(() -> new RuntimeException("Not found Interview"));
  }
}
