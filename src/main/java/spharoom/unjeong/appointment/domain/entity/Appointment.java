package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.global.common.BaseEntity;
import spharoom.unjeong.global.common.CommonException;
import spharoom.unjeong.global.enumeration.AppointmentState;
import spharoom.unjeong.global.enumeration.AppointmentType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Appointment extends BaseEntity { // 200 ~
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;
    @Comment("예약종류")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType appointmentType;
    @Builder.Default
    @Comment("예약상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentState appointmentState = AppointmentState.WAITING;
    @Comment("예약인원")
    private Integer numberOfPeople;
    @Comment("예약날짜")
    @Column(nullable = false)
    private LocalDate appointmentDate;
    @Comment("예약시간")
    @Column(nullable = false)
    private LocalTime appointmentTime;
    @Builder.Default
    @Comment("예약등록시간")
    private LocalDateTime requestDateTime = LocalDateTime.now();
    @Comment("예약메모")
    private String description; // 관리자용

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Appointment toStateDone() {
        if (appointmentState != AppointmentState.WAITING) {
            throw new CommonException(200, "완료될 수 없는 예약입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        appointmentState = AppointmentState.DONE;
        return this;
    }

    public Appointment toStateAltered() { // 기존 데이터는 수정, 변경 데이터는 새 객체로 생성
        if (appointmentState != AppointmentState.WAITING) {
            throw new CommonException(201, "변경할 수 없는 예약입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        appointmentState = AppointmentState.ALTERED;
        deleteData();
        return this;
    }

    public Appointment toStateCanceled() { // daleteData()는 익일 배치로 처리
        if (appointmentState != AppointmentState.WAITING) {
            throw new CommonException(202, "취소할 수 없는 예약입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        appointmentState = AppointmentState.CANCELED;
        return this;
    }

    public Appointment linkToCustomer(Customer customer) {
        this.customer = customer;
        customer.linkToAppointment(this);
        return this;
    }

    public Appointment copyAndAlterAppointment(AlterAppointmentReqDto dto, Customer customer) {
        Appointment copiedAppointment = Appointment.builder()
                .appointmentType(dto.getAlterAppointmentType() != null ? dto.getAlterAppointmentType() : appointmentType)
                .appointmentState(appointmentState)
                .numberOfPeople(dto.getAlterNumberOfPeople() != null ? dto.getAlterNumberOfPeople() : numberOfPeople)
                .appointmentDate(dto.getAlterDate() != null ? dto.getAlterDate() : appointmentDate)
                .appointmentTime(dto.getAlterTime() != null ? dto.getAlterTime() : appointmentTime)
                .requestDateTime(requestDateTime)
                .build()
                .linkToCustomer(customer);
        toStateAltered();
        return copiedAppointment;
    }
}
