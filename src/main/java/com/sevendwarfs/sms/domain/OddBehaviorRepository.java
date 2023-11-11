package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddBehaviorRepository extends JpaRepository<OddBehavior, Long> {

  Optional<OddBehavior> findByBehaviorId(Long behaviorId);

  List<OddBehavior> findByBehaviorTimestampBetween(LocalDateTime start,
      LocalDateTime end);

  void deleteByBehaviorId(Long behaviorID);
}
