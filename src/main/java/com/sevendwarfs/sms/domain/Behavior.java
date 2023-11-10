package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "behaviors")
@NoArgsConstructor
public class Behavior {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "bhv_id")
  private Long id;

  @Column(name = "bhv_captino")
  private String caption;

  @Column(name = "bhv_timestamp")
  private LocalDateTime timestamp;

  @Builder
  public Behavior(String caption) {
    this.caption = caption;
  }

  @Override
  public String toString() {
    return "Behavior{" +
        "id=" + id +
        ", caption='" + caption + '\'' +
        ", timestamp=" + timestamp +
        '}';
  }
}
