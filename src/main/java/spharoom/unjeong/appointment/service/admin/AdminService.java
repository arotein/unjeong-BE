package spharoom.unjeong.appointment.service.admin;

import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.TodayAppointmentByTypeCondition;
import spharoom.unjeong.appointment.dto.request.VacationReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.dto.response.RequiredContactCustomerResDto;
import spharoom.unjeong.appointment.dto.response.TodayAppointmentByTypeDto;
import spharoom.unjeong.appointment.dto.response.VacationResDto;
import spharoom.unjeong.global.enumeration.AccessResult;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    List<VacationResDto> findVacation();

    LocalDate registerVacation(VacationReqDto dto);

    LocalDate cancelVacation(VacationReqDto dto);

    List<AppointmentQueryResDto> findAllCustomerAppointment(AppointmentQueryCondition queryCondition);

    List<RequiredContactCustomerResDto> findRequiredContactCustomer();

    TodayAppointmentByTypeDto findTodayAppointmentByType(TodayAppointmentByTypeCondition condition);

    void updateLoginDateTime(Long adminId);

    void registerLoginLog(String ipAddress, AccessResult accessResult);
}
