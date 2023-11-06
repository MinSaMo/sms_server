package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "dialogs")
public class Dialog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "dlg_id")
  private Long id;

  @Column(name = "dlg_timestamp")
  private LocalDateTime timestamp;
}
