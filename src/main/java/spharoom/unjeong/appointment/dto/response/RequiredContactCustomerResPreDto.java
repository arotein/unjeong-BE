package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.global.enumeration.AppointmentState;
import spharoom.unjeong.global.enumeration.AppointmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequiredContactCustomerResPreDto {
    private Integer index;
    private String name;
    private String phone;
    private String appointmentCode;
    private AppointmentType appointmentType;
    private AppointmentState appointmentState;
    private Integer numberOfPeople;
    private LocalDate appointmentDate;
    private Integer appointmentTime;
    private LocalDateTime requestDateTime;

    public static RequiredContactCustomerResPreDto of(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        return RequiredContactCustomerResPreDto.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
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
