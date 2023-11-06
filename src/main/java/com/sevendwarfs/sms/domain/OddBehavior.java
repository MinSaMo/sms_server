package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity(name = "odd_behaviors")
public class OddBehavior {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "oddb_id")
  private Long id;

  @Column(name = "oddb_reason")
  private String reason;

  @Column(name = "oddb_video_id")
  private Long videoId;

  @OneToOne
  @JoinColumn(name = "bhv_id")
  private Behavior behavior;

}
