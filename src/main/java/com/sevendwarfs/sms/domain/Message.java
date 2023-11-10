package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "messages")
@NoArgsConstructor
public class Message {

  public static final String USER = "user";
  public static final String ASSISTANT = "assistant";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "msg_id")
  private Long id;

  @Column(name = "msg_content")
  private String content;

  @Column(name = "msg_sender")
  private String sender;

  @Column(name = "msg_timestamp")
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "dlg_id")
  private Dialog dialog;

  public void setDialog(Dialog dialog) {
    this.dialog = dialog;
    dialog.getMessageList().add(this);
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", content='" + content + '\'' +
        ", sender='" + sender + '\'' +
        ", timestamp=" + timestamp +
        ", dialog=" + dialog.getId() +
        '}';
  }

  @Builder
  public Message(String content, String sender) {
    this.content = content;
    this.sender = sender;
    timestamp = LocalDateTime.now();
    dialog = null;
  }

  public static Message userMessage(String content) {
    return Message.builder()
        .content(content)
        .sender(USER)
        .build();
  }
  public static Message assistantMessage(String content) {
    return Message.builder()
        .content(content)
        .sender(ASSISTANT)
        .build();
  }

}
