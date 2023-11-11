package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddMessageRepository extends JpaRepository<OddMessage, Long> {

  List<OddMessage> findByMessageTimestampBetween(LocalDateTime start,
      LocalDateTime end);

  Optional<OddMessage> findByMessageId(Long messageId);
  void deleteByMessageId(Long messageId);
}
