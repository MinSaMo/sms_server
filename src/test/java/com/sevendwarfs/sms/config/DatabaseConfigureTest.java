package com.sevendwarfs.sms.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseConfigureTest {

  @Autowired
  DatabaseConfigure configure;

  @Test
  @Transactional
  void 더미데이터_테스트() {
    configure.createMockData();
  }
}