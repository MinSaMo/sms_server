package com.sevendwarfs.sms.config;

import com.sevendwarfs.sms.domain.Behavior;
import com.sevendwarfs.sms.domain.BehaviorRepository;
import com.sevendwarfs.sms.domain.Dialog;
import com.sevendwarfs.sms.domain.DialogRepository;
import com.sevendwarfs.sms.domain.Interview;
import com.sevendwarfs.sms.domain.InterviewLog;
import com.sevendwarfs.sms.domain.InterviewLogRepository;
import com.sevendwarfs.sms.domain.InterviewRepository;
import com.sevendwarfs.sms.domain.Message;
import com.sevendwarfs.sms.domain.MessageRepository;
import com.sevendwarfs.sms.domain.OddBehavior;
import com.sevendwarfs.sms.domain.OddBehaviorRepository;
import com.sevendwarfs.sms.domain.OddMessage;
import com.sevendwarfs.sms.domain.OddMessageRepository;
import com.sevendwarfs.sms.service.BehaviorService;
import com.sevendwarfs.sms.service.DialogService;
import com.sevendwarfs.sms.service.MessageService;
import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;
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

  private final MessageService messageService;
  private final BehaviorService behaviorService;

  private final String TIMEZONE = "Asia/Seoul";

  @PostConstruct
  @Transactional
  public void postConstruct() {
    TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE));
    init();
  }

  @Transactional
  public void init() {
    deleteEntity();
    dialogService.init();
    createMockData();
  }

  @Transactional
  public void createMockData() {

    createFirstWeek();
    log.info("create flight data with First week");
    createSecondWeek();
    log.info("create flight data with Second week");
    createTodayLog();
    log.info("create flight data with Today");
  }

  @Transactional
  public void createFirstWeek() {
    LocalDateTime timestamp = LocalDateTime.of(
        LocalDate.now().getYear(),
        LocalDateTime.now().getMonthValue(),
        1,
        12,
        0);
    Message messageDto = Message.builder()
        .sender(Message.USER)
        .content("test week 1")
        .build();
    messageDto.setTimestamp(timestamp);
    Message message = messageRepository.save(messageDto);

    Behavior behaviorDto = Behavior.builder()
        .caption("11월1주차")
        .build();
    behaviorDto.setTimestamp(timestamp);
    Behavior behavior = behaviorRepository.save(behaviorDto);

    for (int i = 0; i < 5; i++) {
      oddBehaviorRepository.save(OddBehavior.builder()
          .reason("기이행동")
          .videoId(0L)
          .behavior(behavior)
          .build());
    }

    for (int i = 0; i < 10; i++) {
      oddMessageRepository.save(OddMessage.builder()
          .message(message)
          .recognition(new MessageRecognitionDto(true, true, true, true, "test"))
          .build());
    }

    oddMessageRepository.save(OddMessage.builder()
        .message(message)
        .recognition(new MessageRecognitionDto(true, false, false, true, "test"))
        .build());

    oddMessageRepository.save(OddMessage.builder()
        .message(message)
        .recognition(new MessageRecognitionDto(false, false, false, true, "test"))
        .build());
  }

  @Transactional
  public void createSecondWeek() {
    LocalDateTime timestamp = LocalDateTime.of(
        LocalDate.now().getYear(),
        LocalDateTime.now().getMonthValue(),
        8,
        12,
        0);
    Message messageDto = Message.builder()
        .sender(Message.USER)
        .content("test week 2")
        .build();
    messageDto.setTimestamp(timestamp);
    Message message = messageRepository.save(messageDto);

    Behavior behaviorDto = Behavior.builder()
        .caption("11월2주차")
        .build();
    behaviorDto.setTimestamp(timestamp);
    Behavior behavior = behaviorRepository.save(behaviorDto);

    for (int i = 0; i < 7; i++) {
      oddBehaviorRepository.save(OddBehavior.builder()
          .reason("기이행동")
          .videoId(0L)
          .behavior(behavior)
          .build());
    }

    for (int i = 0; i < 12; i++) {
      oddMessageRepository.save(OddMessage.builder()
          .message(message)
          .recognition(new MessageRecognitionDto(true, true, true, true, "test"))
          .build());
    }

    oddMessageRepository.save(OddMessage.builder()
        .message(message)
        .recognition(new MessageRecognitionDto(true, false, false, true, "test"))
        .build());

    oddMessageRepository.save(OddMessage.builder()
        .message(message)
        .recognition(new MessageRecognitionDto(false, false, false, true, "test"))
        .build());
  }

  @Transactional
  public void createTodayLog() {
    // 인터뷰 로그
    LocalDateTime base = LocalDateTime.of(
        LocalDate.now().getYear(),
        LocalDateTime.now().getMonthValue(),
        LocalDateTime.now().getDayOfMonth(),
        11,
        0
    );
    Behavior behaviorDto, behavior;
    Interview interview = interviewRepository.save(Interview.builder()
        .question("약 복용 확인")
        .questionTime(LocalTime.of(11, 0))
        .build());

    saveAssistantMessage(base.withMinute(1), "약을 복용하셨나요? 약을 복용하지 않았다면 지금 복용해주세요.");
    saveUserMessage(base.withMinute(2), "네 약 먹었습니다.");
    saveAssistantMessage(base.withMinute(3), "약을 꾸준히 복용하셔서 좋습니다. 약을 복용하시면 더 나은 결과를 얻을 수 있을 거예요.");

    Dialog curr = dialogService.getCurrentDialog();
    InterviewLog log = InterviewLog.builder()
        .dialog(curr)
        .interview(interview)
        .build();
    log.setTimestamp(base.withMinute(1));
    interviewLogRepository.save(log);
    dialogService.saveNewDialog();

    saveNormalBehavior(base,"He is sitting on a chair and reading a book.");

    base = base.withHour(12);
    saveUserMessage(base.withMinute(21),"안녕 오늘 날씨 정말 좋지?");
    saveAssistantMessage(base.withMinute(22),"네 맞습니다. 오늘 같은날 즐거운 노래를 불러보는 것은 어떨까요?");
    saveUserMessage(base.withMinute(23), "병원에 너무 오래있다 보니까 너무 외롭다.");
    saveAssistantMessage(base.withMinute(23).withSecond(30),
        "병원에서 제공하는 문화적이거나 예술적인 활동에 참여해보세요. 그런 활동을 통해 새로운 경험을 갖는 것은 외로움 극복에 도움이 될것입니다.");
    saveUserOddMessage(base.withMinute(24), "근데 저기 창문너머에 빨간옷을 입은 여자가 나에게 손을 흔들고 있어.",
        new MessageRecognitionDto(false, false, true, false,
            "The possibility of hallucinations describing visual visions in which speech does not actually exist and the symptoms of derailment in a way that speech is not common."));
    saveAssistantMessage(base.withMinute(24).withSecond(30), "그런 상황이라면, 주변에 있는 간호사나 직원에게 알려주시는 것이 좋을 것 같아요.");
    saveUserMessage(base.withMinute(25),"알겠어.");

    saveOddBehavior(base.withMinute(50), "He ate his meal. And he cut his arm with a knife.", 0L,
        "Self-harm refers to severe mental distress or bizarre behavior and is often seen in disorders such as schizophrenia and severe depression");

    dialogService.saveNewDialog();
    base = base.withHour(13);
    saveUserMessage(base.withMinute(52), "아 오늘 너무 춥네.");
    saveAssistantMessage(base.withMinute(52).withSecond(30), "날씨가 추우니 몸 건강에 유의하셔야 해요!");
    saveUserOddMessage(base.withMinute(54), "탈그락 털그락라가라갈가라가",
        new MessageRecognitionDto(true, false, false, true,
            "The user's utterance exhibits signs of linguistic derailment and disorganized language. The phrase '탈그락 털그락라가라갈가라가' does not make coherent sense and lacks logical structure. This suggests a disruption in the user's thought process and organization of language, which are characteristic of linguistic derailment and disorganized language commonly seen in schizophrenia."));
    saveAssistantMessage(base.withMinute(55),"죄송해요, 그 말씀은 이해하지 못했어요. 다른 질문이나 도움이 필요하신가요?");
    saveUserMessage(base.withMinute(56),"아니");
  }

  @Transactional
  public void saveNormalBehavior(LocalDateTime timestamp, String caption) {
    Behavior behaviorDto;
    behaviorDto = Behavior.builder()
        .caption(caption)
        .build();
    behaviorDto.setTimestamp(timestamp);
    behaviorRepository.save(behaviorDto);
  }
  @Transactional
  public void saveOddBehavior(LocalDateTime timestamp, String caption, Long videoId, String reason) {
    Behavior behaviorDto;
    behaviorDto = Behavior.builder()
        .caption(caption)
        .build();
    behaviorDto.setTimestamp(timestamp);
    Behavior behavior = behaviorRepository.save(behaviorDto);
    oddBehaviorRepository.save(OddBehavior.builder()
        .behavior(behavior)
        .videoId(videoId)
        .reason(reason)
        .build());
  }

  @Transactional
  public void saveAssistantMessage(LocalDateTime time,String script) {
    Message messageDto,message;
    messageDto = Message.builder()
        .content(script)
        .sender(Message.ASSISTANT)
        .build();
    messageDto.setDialog(dialogService.getCurrentDialog());
    messageDto.setTimestamp(time);
    message = messageRepository.save(messageDto);
  }
  @Transactional
  public void saveUserMessage(LocalDateTime time,String script) {
    Message messageDto,message;
    messageDto = Message.builder()
        .content(script)
        .sender(Message.USER)
        .build();
    messageDto.setTimestamp(time);
    messageDto.setDialog(dialogService.getCurrentDialog());
    message = messageRepository.save(messageDto);
  }
  @Transactional
  public void saveUserOddMessage(LocalDateTime time,String script, MessageRecognitionDto recognize) {
    Message messageDto,message;
    messageDto = Message.builder()
        .content(script)
        .sender(Message.USER)
        .build();
    messageDto.setTimestamp(time);
    messageDto.setDialog(dialogService.getCurrentDialog());
    message = messageRepository.save(messageDto);

    oddMessageRepository.save(OddMessage.builder()
            .message(message)
            .recognition(recognize)
        .build());
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
