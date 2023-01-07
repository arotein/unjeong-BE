package spharoom.unjeong.appointment.service.customer;

import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentDto;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentForCustomerResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;

import java.util.List;

public interface CustomerService {
    String requestAppointment(RequestAppointmentReqDto dto);

    AppointmentForCustomerResDto findAllAppointmentByNameAndPhone(FindAppointmentDto condition);

    String alterAppointment(String appointmentCode, AlterAppointmentReqDto dto);

    String cancelAppointment(String appointmentCode);

    List<AvailableCheckResDto> availableTimeCheck(AvailableCheckCondition condition);
}
