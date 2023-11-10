package com.sevendwarfs.sms.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddBehaviorRepository extends JpaRepository<OddBehavior, Long> {

  Optional<OddBehavior> findByBehaviorId(Long behaviorId);
}
