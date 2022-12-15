package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByNameAndPhoneAndIsDeletedFalse(String name, String phone);

    List<Appointment> findAllByAppointmentDate(LocalDate appointmentDate);
}