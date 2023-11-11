package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "interview_logs")
public class InterviewLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ilog_id")
  private Long id;

  @Column(name = "ilog_timestamp")
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "ilog_itv_id")
  private Interview interview;

  @Builder
  public InterviewLog(Interview interview) {
    this.interview = interview;
    timestamp = LocalDateTime.now();
  }
}
