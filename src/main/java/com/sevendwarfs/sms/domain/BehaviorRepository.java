package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BehaviorRepository extends JpaRepository<Behavior, Long> {

  Optional<Behavior> findByCaption(String caption);

  List<Behavior> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
