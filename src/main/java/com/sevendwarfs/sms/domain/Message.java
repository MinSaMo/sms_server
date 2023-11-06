package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "messages")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "msg_id")
  private Long id;

  @Column(name = "msg_content")
  private String content;

  @Column(name = "msg_timestamp")
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "mb_id")
  private Member member;
}
