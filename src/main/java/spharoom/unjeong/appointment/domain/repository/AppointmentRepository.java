package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentQueryRepository {
    Optional<Appointment> findByAppointmentCodeAndIsDeletedFalse(String appointmentCode);

    List<Appointment> findAllByAppointmentDateAndIsDeletedFalse(LocalDate appointmentDate);

    Boolean existsByCustomer_IdAndAppointmentDateAndIsDeletedFalse(Long customerId, LocalDate date);
}