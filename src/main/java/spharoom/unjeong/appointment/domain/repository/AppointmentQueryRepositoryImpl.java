package spharoom.unjeong.appointment.domain.repository;

import org.springframework.stereotype.Repository;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.global.common.QuerydslSupport;

@Repository
public class AppointmentQueryRepositoryImpl extends QuerydslSupport implements AppointmentQueryRepository {
    public AppointmentQueryRepositoryImpl() {
        super(Appointment.class);
    }
}
