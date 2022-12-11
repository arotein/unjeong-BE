package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.service.AppointmentService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.Map;

@RestController
@RequestMapping("${global.api.base-path}/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    // 예약신청
    @PostMapping
    public CommonResponse requestAppointment(@RequestBody @Validated RequestAppointmentReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("appointmentId", appointmentService.requestAppointment(dto)))
                .build();
    }
}
