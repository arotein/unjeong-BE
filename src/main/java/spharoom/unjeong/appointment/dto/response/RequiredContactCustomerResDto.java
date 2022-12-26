package spharoom.unjeong.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spharoom.unjeong.appointment.domain.entity.Customer;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredContactCustomerResDto {
    private Integer index;
    private String name;
    private String phone;
    private List<RequiredContactCustomerResPreDto> appointmentList;

    public RequiredContactCustomerResDto(Customer customer, List<RequiredContactCustomerResPreDto> appointmentList) {
        appointmentList.forEach(preDto -> preDto.setIndex(appointmentList.indexOf(preDto)));
        this.name = customer.getName();
        this.phone = customer.getPhone();
        this.appointmentList = appointmentList;
    }
}
