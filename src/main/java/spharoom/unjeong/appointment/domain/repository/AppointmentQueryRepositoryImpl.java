package spharoom.unjeong.appointment.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.global.common.QuerydslSupport;
import spharoom.unjeong.global.enumeration.AppointmentState;

import java.time.LocalDate;
import java.util.List;

import static spharoom.unjeong.appointment.domain.entity.QAppointment.appointment;
import static spharoom.unjeong.appointment.domain.entity.QCustomer.customer;
import static spharoom.unjeong.appointment.domain.entity.QVacation.vacation;

@Repository
public class AppointmentQueryRepositoryImpl extends QuerydslSupport implements AppointmentQueryRepository {
    public AppointmentQueryRepositoryImpl() {
        super(Appointment.class);
    }

    @Override
    public List<Appointment> findAllRequiredContactCustomer() {
        LocalDate nowDate = LocalDate.now();
        // 휴가일 탐색
        JPQLQuery<LocalDate> subQuery = JPAExpressions
                .select(vacation.vacationDate)
                .from(vacation)
                .where(dateBetween(nowDate, nowDate.plusWeeks(1)));

        // 먼저 예약했으나 휴가일과 겹친 예약 조회
        return selectFrom(appointment)
                .join(appointment.customer, customer).fetchJoin()
                .where(notDeleted(),
                        appointment.appointmentState.eq(AppointmentState.WAITING),
                        appointment.appointmentDate.in(subQuery))
                .fetch();
    }

    @Override
    public List<Appointment> findAllAppointmentTwoWeeks(AppointmentQueryCondition queryCondition) {
        return selectFrom(appointment)
                .join(appointment.customer, customer).fetchJoin()
                .where(notDeleted(),
                        dateBetween(queryCondition.getDate().minusWeeks(1), queryCondition.getDate().plusWeeks(1)))
                .orderBy(appointment.appointmentDate.desc(), appointment.appointmentTime.desc())
                .fetch();
    }

    @Override
    public void stateToDoneOfDateWithBatch(LocalDate date) { // 전날 예약을 모두 '상담완료'로 변경
        update(appointment)
                .set(appointment.appointmentState, AppointmentState.DONE)
                .where(appointment.appointmentState.eq(AppointmentState.WAITING),
                        appointment.appointmentDate.eq(date),
                        notDeleted())
                .execute();
    }

    @Override
    public void deleteAppointmentOfDateWithBatch(LocalDate date) { // 전날 취소된 예약을 조회불가하게 delete
        update(appointment)
                .set(appointment.isDeleted, true)
                .where(appointment.appointmentState.eq(AppointmentState.CANCELED),
                        appointment.appointmentDate.eq(date),
                        notDeleted())
                .execute();
    }

    private BooleanExpression notDeleted() {
        return appointment.isDeleted.isFalse();
    }

    private BooleanExpression dateEq(LocalDate date) {
        return date != null ? appointment.appointmentDate.eq(date) : null;
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? appointment.appointmentDate.between(startDate, endDate) : null;
    }
}
