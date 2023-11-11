package com.sevendwarfs.sms.service;

import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.DialogRepository;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.service.dto.gpt.SummarizeResponseDto;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DialogService {

  private final DialogRepository dialogRepository;
  private final GptService gptService;
  private final PromptManager promptManager;

  @Getter
  private Dialog currentDialog;
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private String recentSummary;

  @PostConstruct
  protected void init() {
    saveNewDialog();
    recentSummary = "";
    log.info("init dialog={}", currentDialog);
  }

  @Transactional
  public Dialog getDialogById(Long dialogId) {
    return dialogRepository.findById(dialogId)
        .orElseThrow(() -> new RuntimeException("Dialog not found"));
  }

  public void endDialog() {
    summarizeDialog();
    saveNewDialog();
  }

  public void summarizeDialog() {
    Prompt prompt = promptManager.getSummarizeDialog();
    ChatCompletionRequest request = addCurrentDialog(
        gptService
            .request()
            .addUserMessage(previousSummarization()))
        .addSystemMessage(prompt.getScript())
        .topP(prompt.getTopP())
        .temperature(prompt.getTemperature())
        .build();
    SummarizeResponseDto response = gptService.ask(request, SummarizeResponseDto.class);
    recentSummary = response.summary();
  }

  private String previousSummarization() {
    return String.format("Previous Summary: %s", recentSummary);
  }

  public ChatCompletionRequest.Builder addCurrentDialog(ChatCompletionRequest.Builder builder) {
    for (Message message : currentDialog.getMessageList()) {
      if (message.getSender().equals(Message.USER)) {
        builder.addUserMessage(message.getContent());
      } else {
        builder.addAssistantMessage(message.getContent());
      }
    }
    return builder;
  }


  private void saveNewDialog() {
    currentDialog = dialogRepository.save(new Dialog());
    log.info("new dialog={}", currentDialog);
  }
}
