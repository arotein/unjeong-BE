package spharoom.unjeong.appointment.domain.repository;

import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentQueryRepository {
    List<Appointment> findAllRequiredContactCustomer();

    List<Appointment> findAllAppointmentTwoWeeks(AppointmentQueryCondition queryCondition);

    void stateToDoneOfDateWithBatch(LocalDate date);

    void deleteAppointmentOfDateWithBatch(LocalDate date);
}
