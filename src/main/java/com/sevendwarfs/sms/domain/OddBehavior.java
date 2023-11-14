package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "odd_behaviors")
@NoArgsConstructor
public class OddBehavior {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "oddb_id")
  private Long id;

  @Column(name = "oddb_reason", length = 1000)
  private String reason;

  @Column(name = "oddb_video_id")
  private Long videoId;

  @ManyToOne
  @JoinColumn(name = "bhv_id")
  private Behavior behavior;

  @Builder
  public OddBehavior(String reason, Long videoId, Behavior behavior) {
    this.reason = reason;
    this.videoId = videoId;
    this.behavior = behavior;
  }
}
