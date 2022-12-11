package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}