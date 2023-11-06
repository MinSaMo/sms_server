package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity(name = "odd_messages")
public class OddMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "oddm_id")
  private Long id;

  @Setter
  @Column(name = "oddm_reason")
  private String reason;

  @OneToOne
  @JoinColumn(name = "msg_id")
  private Message message;
}
