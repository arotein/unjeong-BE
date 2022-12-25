package spharoom.unjeong.appointment.domain.repository;

import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentQueryRepository {
    List<Appointment> findAllRequiredContactCustomer();

    List<Appointment> findAllAppointmentTwoWeeks(AppointmentQueryCondition queryCondition);

    Appointment findLastAppointmentWithCustomer(String name, String phone, LocalDate appointmentDate);

    Appointment findAppointmentByDateAndTime(LocalDate appointmentDate, LocalTime appointmentTime);

    List<Appointment> findAllByCustomer(Long customerId);

    void stateToDoneOfDateWithBatch(LocalDate date);

    void deleteAppointmentOfDateWithBatch(LocalDate date);
}
