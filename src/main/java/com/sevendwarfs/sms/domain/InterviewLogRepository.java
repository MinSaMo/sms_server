package com.sevendwarfs.sms.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewLogRepository extends JpaRepository<InterviewLog, Long> {

  List<InterviewLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
