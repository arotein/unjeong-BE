package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.global.enumeration.AppointmentState;
import spharoom.unjeong.global.enumeration.AppointmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class AppointmentForCustomerResDto {
    private String name;
    private List<InnerDto> appointmentList;

    public AppointmentForCustomerResDto(String name, List<InnerDto> appointmentList) {
        appointmentList.forEach(inner -> inner.setIndex(appointmentList.indexOf(inner)));
        this.name = name;
        this.appointmentList = appointmentList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerDto {
        private Integer index;
        private String appointmentCode;
        private AppointmentType appointmentType;
        private AppointmentState appointmentState;
        private Integer numberOfPeople;
        private LocalDate appointmentDate;
        private Integer appointmentTime;
        private LocalDateTime requestDateTime;

        public static InnerDto of(Appointment appointment) {
            return InnerDto.builder()
                    .appointmentCode(appointment.getAppointmentCode())
                    .appointmentType(appointment.getAppointmentType())
                    .appointmentState(appointment.getAppointmentState())
                    .numberOfPeople(appointment.getNumberOfPeople())
                    .appointmentDate(appointment.getAppointmentDate())
                    .appointmentTime(appointment.getAppointmentTime().getHour())
                    .requestDateTime(appointment.getRequestDateTime())
                    .build();
        }

        public String getRequestDateTime() {
            return requestDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        public String getAppointmentType() {
            return appointmentType.getDescription();
        }

        public String getAppointmentState() {
            return appointmentState.getDescription();
        }
    }
}