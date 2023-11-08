package com.sevendwarfs.sms.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfigure {

  private static final String dateFormat = "yyyy-MM-dd";
  private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
  private static final String timeZone = "Asia/Seoul";

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {

      JavaTimeModule jtm = new JavaTimeModule();
      jtm.addSerializer(LocalDate.class,
          new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
      jtm.addDeserializer(LocalDate.class,
          new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
      jtm.addSerializer(LocalDateTime.class,
          new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
      jtm.addDeserializer(LocalDateTime.class,
          new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

      builder.timeZone(TimeZone.getTimeZone(timeZone));
      builder.modulesToInstall(jtm);
    };
  }
}
