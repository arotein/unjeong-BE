package spharoom.unjeong.appointment.domain.repository;

import java.time.LocalDate;

public interface AppointmentQueryRepository {
    void stateToDoneOfDateWithBatch(LocalDate date);

    void deleteAppointmentOfDateWithBatch(LocalDate date);
}
