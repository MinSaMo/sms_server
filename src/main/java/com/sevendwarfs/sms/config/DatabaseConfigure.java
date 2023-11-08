package com.sevendwarfs.sms.config;

import com.sevendwarfs.sms.domain.Member;
import com.sevendwarfs.sms.domain.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfigure {

  private final MemberRepository memberRepository;

  @PostConstruct
  private void init() {
    Member saved = memberRepository.save(Member.builder()
        .age(-1)
        .name("assistant")
        .build());

    log.info("[DB INIT] system member created : {}", saved);
  }

}
