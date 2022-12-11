package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import spharoom.unjeong.global.common.BaseEntity;
import spharoom.unjeong.global.enumeration.AppointmentType;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;
    private Integer numberOfPeople;
    private LocalDateTime appointmentDateTime;
    private String description;
}
