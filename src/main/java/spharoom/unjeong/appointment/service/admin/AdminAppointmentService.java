package spharoom.unjeong.appointment.service.admin;

import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;

import java.util.List;

public interface AdminAppointmentService {
    List<AppointmentQueryResDto> findAllCustomerAppointment(AppointmentQueryCondition queryCondition);
}
