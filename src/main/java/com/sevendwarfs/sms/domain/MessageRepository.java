package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
