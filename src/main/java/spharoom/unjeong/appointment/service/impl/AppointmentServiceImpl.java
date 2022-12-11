package spharoom.unjeong.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.service.AppointmentService;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    public Long requestAppointment(RequestAppointmentReqDto dto) {
        // 중복예약 체크(하루에 한 팀만)
        // 동시성 이슈 처리
        // 카톡 안내메세지 외부 API 호출(비동기처리)
        return appointmentRepository.save(dto.toEntity()).getId();
    }
}
