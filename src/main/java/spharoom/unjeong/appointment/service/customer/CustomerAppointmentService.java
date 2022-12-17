package spharoom.unjeong.appointment.service.customer;

import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;

import java.util.List;

public interface CustomerAppointmentService {
    Long requestAppointment(RequestAppointmentReqDto dto);

    List<AppointmentResDto> findAllAppointmentByNameAndPhone(FindAppointmentCondition condition);

    Long alterAppointment(Long appointmentId, AlterAppointmentReqDto dto);

    Long cancelAppointment(Long appointmentId);

    AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition);
}
