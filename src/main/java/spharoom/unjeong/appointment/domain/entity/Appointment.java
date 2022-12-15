package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import spharoom.unjeong.global.common.BaseEntity;
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
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;
    @Comment("예약자")
    private String name;
    @Comment("연락처")
    private String phone;
    @Comment("예약종류")
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;
    @Comment("예약인원")
    private Integer numberOfPeople;
    @Comment("예약날짜")
    private LocalDate appointmentDate;
    @Comment("예약시간")
    private LocalTime appointmentTime;
    @Comment("예약요청시간")
    @Builder.Default
    private LocalDateTime requestDateTime = LocalDateTime.now();
    @Comment("예약메모")
    private String description; // 관리자용
}
