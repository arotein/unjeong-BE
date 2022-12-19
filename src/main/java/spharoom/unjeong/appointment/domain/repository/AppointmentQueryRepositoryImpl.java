package spharoom.unjeong.appointment.domain.repository;

import org.springframework.stereotype.Repository;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.global.common.QuerydslSupport;
import spharoom.unjeong.global.enumeration.AppointmentState;

import java.time.LocalDate;

import static spharoom.unjeong.appointment.domain.entity.QAppointment.appointment;

@Repository
public class AppointmentQueryRepositoryImpl extends QuerydslSupport implements AppointmentQueryRepository {
    public AppointmentQueryRepositoryImpl() {
        super(Appointment.class);
    }

    @Override
    public void stateToDoneOfDateWithBatch(LocalDate date) { // 전날 예약을 모두 '상담완료'로 변경
        update(appointment)
                .set(appointment.appointmentState, AppointmentState.DONE)
                .where(appointment.appointmentState.eq(AppointmentState.WAITING),
                        appointment.appointmentDate.eq(date),
                        appointment.isDeleted.isFalse())
                .execute();
    }

    @Override
    public void deleteAppointmentOfDateWithBatch(LocalDate date) { // 전날 취소된 예약을 조회불가하게 delete
        update(appointment)
                .set(appointment.isDeleted, true)
                .where(appointment.appointmentState.eq(AppointmentState.CANCELED),
                        appointment.appointmentDate.eq(date),
                        appointment.isDeleted.isFalse())
                .execute();
    }
}
