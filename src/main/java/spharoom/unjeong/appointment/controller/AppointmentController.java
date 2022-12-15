package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
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

    // 이름과 전화번호로 예약 조회
    @GetMapping
    public CommonResponse findAppointmentByNameAndPhone(@Validated FindAppointmentCondition condition) {
        return CommonResponse.builder()
                .data(appointmentService.findAppointmentByNameAndPhone(condition))
                .build();
    }

    // 예약 가능여부 조회
    @GetMapping("/available")
    public CommonResponse availableTimeCheck(@Validated AvailableCheckCondition condition) {
        return CommonResponse.builder()
                .data(appointmentService.availableTimeCheck(condition))
                .build();
    }
}
