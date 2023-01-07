package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.AccessAttemptLog;

public interface AccessAttemptLogRepository extends JpaRepository<AccessAttemptLog, Long> {
}
