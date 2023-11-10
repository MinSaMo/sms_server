package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Entity(name = "dialogs")
public class Dialog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "dlg_id")
  private Long id;

  @OneToMany(mappedBy = "dialog")
  private List<Message> messageList;

  public Dialog() {
    this.messageList = new ArrayList<>();
  }

  @Override
  public String toString() {
    return "Dialog{" +
        "id=" + id +
        "messageSize=" + messageList.size() +
        '}';
  }
}
