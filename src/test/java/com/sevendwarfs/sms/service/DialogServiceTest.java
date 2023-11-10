package com.sevendwarfs.sms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.DialogRepository;
import com.sevendwarfs.sms.global.exception.GptRemoteServerError;
import dev.ai4j.openai4j.OpenAiHttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class DialogServiceTest {

  @Autowired
  DialogService dialogService;
  @Autowired
  MessageService messageService;
  @Autowired
  DialogRepository dialogRepository;

  @BeforeEach
  void beforeEach() {
    dialogService.init();
    dialogRepository.deleteAll();
  }

  @Test
  @Transactional
  void 대화_초기화_테스트() {
    Dialog currentDialog = dialogService.getCurrentDialog();
    String recentSummary = dialogService.getRecentSummary();

    assertNotNull(currentDialog);
    assertEquals(recentSummary, "");
  }

  @Test
  @Transactional
  void 대화_조회_테스트_성공() {
    Dialog saved = dialogRepository.save(new Dialog());
    Dialog found = dialogService.getDialogById(saved.getId());
    assertEquals(saved.getId(), found.getId());
  }

  @Test
  @Transactional
  void 대화_조회_테스트_실패() {
    Dialog saved = dialogRepository.save(new Dialog());
    assertThrows(RuntimeException.class,
        () -> dialogService.getDialogById(saved.getId() + 1));
  }

  @Test
  @Transactional
  void 대화_요약_테스트() {
    try {
      String beforeSummary = "Users are also interested in exercise.";
      messageService.createUserMessage("Hello");
      messageService.createAssistantMessage("Hi, Welcome to our service");
      messageService.createUserMessage("How does your service work?");
      messageService.createAssistantMessage(
          "Our service helps you with a wide range of tasks. Please let me know what you need assistance with.");
      messageService.createUserMessage("I need help with finding a recipe for dinner.");
      messageService.createAssistantMessage(
          "Sure! What type of cuisine or ingredients are you interested in for dinner?");
      dialogService.setRecentSummary(beforeSummary);

      dialogService.summarizeDialog();

      String recentSummary;
      try {
        recentSummary = dialogService.getRecentSummary();
      } catch (OpenAiHttpException e) {
        return;
      }
      assertNotEquals(recentSummary, beforeSummary);

    } catch (GptRemoteServerError error) {
      return;
    }
  }

}