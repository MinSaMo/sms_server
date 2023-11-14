package com.sevendwarfs.sms.domain;

import com.sevendwarfs.sms.service.dto.gpt.MessageRecognitionDto;
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
import lombok.Setter;

@Getter
@Entity(name = "odd_messages")
@NoArgsConstructor
public class OddMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "oddm_id")
  private Long id;

  @Setter
  @Column(name = "oddm_reason", length = 1000)
  private String reason;

  /*
  언어의 탈선
   */
  @Column(name = "oddm_ling")
  private Boolean isLinguisticDerailment;

  /*
  환각
   */
  @Column(name = "oddm_delu")
  private Boolean isDelusions;

  /*
  망상
   */
  @Column(name = "oddm_hallu")
  private Boolean isHallucination;

  /*
  와해된 언어
   */
  @Column(name = "oddm_disorganized")
  private Boolean isDisorganized;

  @ManyToOne
  @JoinColumn(name = "msg_id")
  private Message message;

  @Builder
  public OddMessage(Message message, MessageRecognitionDto recognition) {
    this.message = message;
    this.isLinguisticDerailment = recognition.linguisticDerailment();
    this.isDelusions = recognition.delusions();
    this.isHallucination = recognition.hallucination();
    this.isDisorganized = recognition.disorganizedLanguage();
    this.reason = recognition.rationale();
  }

  @Override
  public String toString() {
    return "OddMessage{" +
        "id=" + id +
        ", 근거='" + reason + '\'' +
        ", 언어의 탈선=" + isLinguisticDerailment +
        ", 환각=" + isDelusions +
        ", 망각=" + isHallucination +
        ", 와해된 언어=" + isDisorganized +
        ", message=" + message.getContent() +
        '}';
  }

  public String symptomString() {
    StringBuilder sb = new StringBuilder();
    if (this.getIsDisorganized()) {
      sb.append("와해된 언어 ");
    }
    if (this.getIsDelusions()) {
      sb.append("환각 ");
    }
    if (this.getIsLinguisticDerailment()) {
      sb.append("언어의 탈선 ");
    }
    if (this.getIsHallucination()) {
      sb.append("망상 ");
    }
    sb.append("증상");
    return sb.toString();
  }

}
