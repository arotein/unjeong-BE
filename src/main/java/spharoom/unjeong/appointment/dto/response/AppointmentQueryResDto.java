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
public class AppointmentQueryResDto {
    private Integer index;
    private LocalDate date;
    private List<AppointmentForAdminDto> customerList;

    public AppointmentQueryResDto(LocalDate date, List<AppointmentForAdminDto> customerList) {
        customerList.forEach(preDto -> preDto.setIndex(customerList.indexOf(preDto)));
        this.date = date;
        this.customerList = customerList;
    }
}
