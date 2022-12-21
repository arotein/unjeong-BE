package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.global.enumeration.AppointmentState;
import spharoom.unjeong.global.enumeration.AppointmentType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentQueryResPreDto {
    private Integer index;
    private String name;
    private String phone;
    private AppointmentType appointmentType;
    private AppointmentState appointmentState;
    private Integer numberOfPeople;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public static AppointmentQueryResPreDto of(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        return AppointmentQueryResPreDto.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .appointmentType(appointment.getAppointmentType())
                .appointmentState(appointment.getAppointmentState())
                .numberOfPeople(appointment.getNumberOfPeople())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .build();
    }

    public String getAppointmentType() {
        return appointmentType.getDescription();
    }

    public String getAppointmentState() {
        return appointmentState.getDescription();
    }
}
