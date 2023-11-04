package com.sevendwarfs.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SmsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmsApplication.class, args);
  }

}
