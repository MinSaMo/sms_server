package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddMessageRepository extends JpaRepository<OddMessage, Long> {

  List<OddMessage> findByMessageTimestampBetween(LocalDateTime startOfMonth,
      LocalDateTime endOfMonth);
}
