package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResDto {
    private Long requestId;
    private String name;
    private String appointmentType;
    private String appointmentState;
    private Integer numberOfPeople;
    private LocalDate appointmentDate;
    private Integer appointmentTime;
    private LocalDateTime requestDateTime;

    public static AppointmentResDto of(Appointment appointment) {
        return AppointmentResDto.builder()
                .requestId(appointment.getId())
                .name(appointment.getCustomer().getName())
                .appointmentType(appointment.getAppointmentType().getDescription())
                .appointmentState(appointment.getAppointmentState().getDescription())
                .numberOfPeople(appointment.getNumberOfPeople())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime().getHour())
                .requestDateTime(appointment.getRequestDateTime())
                .build();
    }

    public String getRequestDateTime() {
        return requestDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}