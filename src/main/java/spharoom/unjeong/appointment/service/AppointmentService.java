package spharoom.unjeong.appointment.service;

import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;

public interface AppointmentService {
    Long requestAppointment(RequestAppointmentReqDto dto);
}
