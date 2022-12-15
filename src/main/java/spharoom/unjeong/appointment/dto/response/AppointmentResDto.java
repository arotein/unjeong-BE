package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Appointment;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResDto {
    private Long requestId;
    private String name;
    private String appointmentType;
    private Integer numberOfPeople;
    private LocalDate appointmentDate;
    private Integer appointmentTime;

    public static AppointmentResDto of(Appointment appointment) {
        return AppointmentResDto.builder()
                .requestId(appointment.getId())
                .name(appointment.getName())
                .appointmentType(appointment.getAppointmentType().toString())
                .numberOfPeople(appointment.getNumberOfPeople())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime().getHour())
                .build();
    }
}
