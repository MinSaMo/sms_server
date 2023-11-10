package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "interviews")
@NoArgsConstructor
public class Interview {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "itv_id")
  private Long id;

  @Column(name = "itv_question_id")
  private String question;

  @Column(name = "itv_time")
  private LocalTime questionTime;

  @OneToMany(mappedBy = "interview")
  private List<InterviewDialog> dialogList;

  @Builder
  public Interview(String question, LocalTime questionTime) {
    this.question = question;
    this.questionTime = questionTime;
    dialogList = new ArrayList<>();
  }
}
