package spharoom.unjeong.appointment.service.customer;

import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;

public interface CustomerAppointmentService {
    String requestAppointment(RequestAppointmentReqDto dto);

    AppointmentResDto findAllAppointmentByNameAndPhone(FindAppointmentCondition condition);

    String alterAppointment(String appointmentCode, AlterAppointmentReqDto dto);

    String cancelAppointment(String appointmentCode);

    AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition);
}
