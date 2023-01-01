package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spharoom.unjeong.global.enumeration.AppointmentType;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodayAppointmentByTypeCondition {
    @NotNull
    private AppointmentType appointmentType;
}
