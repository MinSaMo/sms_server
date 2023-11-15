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
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity(name = "interview_logs")
public class InterviewLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ilog_id")
  private Long id;

  @Setter
  @Column(name = "ilog_timestamp")
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "ilog_itv_id")
  private Interview interview;

  @ManyToOne
  @JoinColumn(name = "ilog_dlg_id")
  private Dialog dialog;

  @Builder
  public InterviewLog(Interview interview, Dialog dialog) {
    this.interview = interview;
    this.dialog = dialog;
    timestamp = LocalDateTime.now();
  }
}
