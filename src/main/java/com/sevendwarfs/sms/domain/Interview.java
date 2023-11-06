package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity(name = "interviews")
public class Interview {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "itv_id")
  private Long id;

  @Column(name = "itv_question_id")
  private String question;

  @Column(name = "itv_time")
  private LocalDateTime questionTime;

  @OneToOne
  @JoinColumn(name = "itv_dialog_id")
  private Dialog dialog;
}
