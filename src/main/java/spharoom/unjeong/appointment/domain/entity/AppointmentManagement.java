package spharoom.unjeong.appointment.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AppointmentManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_management_id")
    private Long id;

    private String memo;

    @OneToOne(mappedBy = "appointmentManagement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Appointment appointment;

    public void linkToAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
