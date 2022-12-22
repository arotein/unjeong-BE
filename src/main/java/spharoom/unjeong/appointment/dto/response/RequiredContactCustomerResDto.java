package spharoom.unjeong.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredContactCustomerResDto {
    private Integer index;
    private LocalDate date;
    private List<RequiredContactCustomerResPreDto> customerList;

    public RequiredContactCustomerResDto(LocalDate appointmentDate, List<RequiredContactCustomerResPreDto> customerList) {
        customerList.forEach(preDto -> preDto.setIndex(customerList.indexOf(preDto)));
        this.date = appointmentDate;
        this.customerList = customerList;
    }
}
