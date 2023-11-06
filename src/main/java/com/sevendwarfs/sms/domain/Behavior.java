package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "behaviors")
public class Behavior {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "bhv_id")
  private Long id;

  @Column(name = "bhv_captino")
  private String caption;

  @Column(name = "bhv_timestamp")
  private LocalDateTime timestamp;
}
