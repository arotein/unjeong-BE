package spharoom.unjeong.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMinDateDto {
    private Customer customer;
    private LocalDateTime minDateTime;

    public CustomerMinDateDto(Appointment appointment) {
        this.customer = appointment.getCustomer();
        this.minDateTime = LocalDateTime.of(appointment.getAppointmentDate(), appointment.getAppointmentTime());
    }
}
