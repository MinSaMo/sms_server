package com.sevendwarfs.sms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @ToString
@Entity(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "mb_id")
  private Long id;

  @Setter
  @Column(name = "mb_name")
  private String name;

  @Setter
  @Column(name = "mb_age")
  private Integer age;

  @OneToMany(mappedBy = "member")
  private List<Message> messageList;

  @Builder
  public Member(String name, Integer age) {
    this.name = name;
    this.age = age;

    messageList = new ArrayList<>();
  }
}
