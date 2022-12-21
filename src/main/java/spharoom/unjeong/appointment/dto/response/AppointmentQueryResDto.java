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
    private LocalDate appointmentDate;
    private List<AppointmentQueryResPreDto> appointmentList;

    public AppointmentQueryResDto(LocalDate appointmentDate, List<AppointmentQueryResPreDto> appointmentList) {
        appointmentList.forEach(preDto -> preDto.setIndex(appointmentList.indexOf(preDto)));
        this.appointmentDate = appointmentDate;
        this.appointmentList = appointmentList;
    }
}
