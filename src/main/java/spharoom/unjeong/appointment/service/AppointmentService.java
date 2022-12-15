package spharoom.unjeong.appointment.service;

import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;

public interface AppointmentService {
    Long requestAppointment(RequestAppointmentReqDto dto);

    AppointmentResDto findAppointmentByNameAndPhone(FindAppointmentCondition condition);

    AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition);
}
