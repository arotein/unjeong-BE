package spharoom.unjeong.appointment.service.admin;

import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.VacationReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.dto.response.RequiredContactCustomerResDto;

import java.time.LocalDate;
import java.util.List;

public interface AdminAppointmentService {
    LocalDate registerVacation(VacationReqDto dto);

    LocalDate cancelVacation(VacationReqDto dto);

    List<AppointmentQueryResDto> findAllCustomerAppointment(AppointmentQueryCondition queryCondition);

    List<RequiredContactCustomerResDto> findRequiredContactCustomer();
}
