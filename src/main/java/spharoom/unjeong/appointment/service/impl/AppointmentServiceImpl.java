package spharoom.unjeong.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;
import spharoom.unjeong.appointment.service.AppointmentService;
import spharoom.unjeong.global.common.CommonException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService { // 100~
    private final AppointmentRepository appointmentRepository;

    @Override
    public Long requestAppointment(RequestAppointmentReqDto dto) {
        // 중복예약 체크(하루에 한 팀만)
        // 동시성 이슈 처리
        // 카톡 안내메세지 외부 API 호출(비동기처리)
        return appointmentRepository.save(dto.toEntity()).getId();
    }

    @Override
    public AppointmentResDto findAppointmentByNameAndPhone(FindAppointmentCondition condition) {
        Appointment appointment = appointmentRepository.findByNameAndPhoneAndIsDeletedFalse(condition.getName(), condition.getPhone())
                .orElseThrow(() -> new CommonException(100, "7일 이내에 예약된 이력이 없습니다.", HttpStatus.BAD_REQUEST));

        return AppointmentResDto.of(appointment);
    }

    @Override
    public AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition) {
        List<Appointment> appointmentList = appointmentRepository.findAllByAppointmentDate(condition.getDate());
        List<Integer> availableTimeList = generateAvailableTime();
        appointmentList.forEach(appointment -> {
            int hour = appointment.getAppointmentTime().getHour();
            availableTimeList.forEach(time -> {
                if (time == hour) {
                    availableTimeList.remove(time);
                }
            });
        });
        return new AvailableCheckResDto(availableTimeList);
    }

    private List<Integer> generateAvailableTime() {
        List<Integer> timeList = new ArrayList<>();
        for (int k = 11; k <= 19; k++) {
            timeList.add(k);
        }
        return timeList;
    }
}
