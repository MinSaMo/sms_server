package com.sevendwarfs.sms.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BehaviorRepository extends JpaRepository<Behavior, Long> {

  Optional<Behavior> findByCaption(String caption);
}
