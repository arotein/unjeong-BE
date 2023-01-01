package spharoom.unjeong.appointment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spharoom.unjeong.global.enumeration.AppointmentType;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TodayAppointmentByTypeDto {
    private AppointmentType appointmentType;
    private List<AppointmentForAdminDto> customerList;

    public TodayAppointmentByTypeDto(AppointmentType appointmentType, List<AppointmentForAdminDto> customerList) {
        customerList.forEach(innerDto -> innerDto.setIndex(customerList.indexOf(innerDto)));
        this.appointmentType = appointmentType;
        this.customerList = customerList;
    }

    public String getAppointmentType() {
        return appointmentType.getDescription();
    }
}