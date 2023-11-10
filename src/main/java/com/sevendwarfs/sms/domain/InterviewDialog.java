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

@Entity(name = "interview_dialogs")
@Getter
@NoArgsConstructor
public class InterviewDialog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "itvdlg_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "itvdlg_dlg_id")
  private Dialog dialog;

  @ManyToOne
  @JoinColumn(name = "itvdlg_itv_id")
  private Interview interview;

  @Builder
  public InterviewDialog(Dialog dialog, Interview interview) {
    this.dialog = dialog;
    this.interview = interview;
    interview.getDialogList().add(this);
  }
}
