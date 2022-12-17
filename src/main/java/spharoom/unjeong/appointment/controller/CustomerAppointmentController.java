package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.service.customer.CustomerAppointmentService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.Map;

@RestController
@RequestMapping("${global.api.customer-base-path}/appointment")
@RequiredArgsConstructor
public class CustomerAppointmentController {
    private final CustomerAppointmentService customerAppointmentService;

    // 예약신청
    @PostMapping
    public CommonResponse requestAppointment(@RequestBody @Validated RequestAppointmentReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("appointmentId", customerAppointmentService.requestAppointment(dto.checkValidation())))
                .build();
    }

    // (이름과 전화번호로) 예약 조회 (오늘~7일후 조회)
    @GetMapping
    public CommonResponse findAppointmentByNameAndPhone(@Validated FindAppointmentCondition condition) {
        return CommonResponse.builder()
                .data(customerAppointmentService.findAllAppointmentByNameAndPhone(condition))
                .build();
    }

    // 해당날짜 예약 가능시간 조회
    @GetMapping("/available")
    public CommonResponse availableTimeCheck(@Validated AvailableCheckCondition condition) {
        return CommonResponse.builder()
                .data(customerAppointmentService.availableTimeCheck(condition.checkValidation()))
                .build();
    }

    // 예약 변경
    @PatchMapping("/{appointmentId}")
    public CommonResponse alterAppointment(@PathVariable Long appointmentId,
                                           @RequestBody @Validated AlterAppointmentReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("appointmentId", customerAppointmentService.alterAppointment(appointmentId, dto.checkValidation())))
                .build();
    }

    // 예약 취소
    @DeleteMapping("/{appointmentId}")
    public CommonResponse cancelAppointment(@PathVariable Long appointmentId) {
        return CommonResponse.builder()
                .data(Map.of("appointmentId", customerAppointmentService.cancelAppointment(appointmentId)))
                .build();
    }
}