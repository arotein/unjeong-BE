package spharoom.unjeong.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredContactCustomerResDto {
    private Integer index;
    private String name;
    private String phone;
    private List<RequiredContactCustomerResPreDto> appointmentList;

    public RequiredContactCustomerResDto(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        this.name = customer.getName();
        this.phone = customer.getPhone();
        this.appointmentList = new ArrayList<>();
        appointmentList.add(RequiredContactCustomerResPreDto.of(appointment));
    }

    public RequiredContactCustomerResDto addToAppointmentList(Appointment appointment) {
        appointmentList.add(RequiredContactCustomerResPreDto.of(appointment));
        return this;
    }

    public Boolean samePeopleCheck(Customer customer) {
        return Objects.equals(this.name, customer.getName())
                && Objects.equals(this.phone, customer.getPhone())
                ? true : false;
    }

    public RequiredContactCustomerResDto addIndex() {
        appointmentList.forEach(appointment -> appointment.setIndex(appointmentList.indexOf(appointment)));
        return this;
    }
}
