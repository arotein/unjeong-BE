package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentDto;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.service.customer.CustomerService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.Map;

@RestController
@RequestMapping("${global.api.customer-base-path}/appointment")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // 예약신청
    @PostMapping
    public CommonResponse requestAppointment(@RequestBody @Validated RequestAppointmentReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("appointmentCode", customerService.requestAppointment(dto.checkValidation())))
                .build();
    }

    // (이름과 전화번호로) 예약 조회 (오늘~7일후 조회)
    @PostMapping("/my")
    public CommonResponse findAppointmentByNameAndPhone(@RequestBody FindAppointmentDto dto) {
        return CommonResponse.builder()
                .data(customerService.findAllAppointmentByNameAndPhone(dto.validate()))
                .build();
    }

    // 해당날짜 예약 가능시간 조회
    @GetMapping("/available")
    public CommonResponse availableTimeCheck(@Validated AvailableCheckCondition condition) {
        return CommonResponse.builder()
                .data(customerService.availableTimeCheck(condition.checkValidation()))
                .build();
    }

    // 예약 변경
    @PatchMapping("/{appointmentCode}")
    public CommonResponse alterAppointment(@PathVariable String appointmentCode,
                                           @RequestBody @Validated AlterAppointmentReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("appointmentCode", customerService.alterAppointment(appointmentCode, dto.checkValidation())))
                .build();
    }

    // 예약 취소
    @DeleteMapping("/{appointmentCode}")
    public CommonResponse cancelAppointment(@PathVariable String appointmentCode) {
        return CommonResponse.builder()
                .data(Map.of("appointmentCode", customerService.cancelAppointment(appointmentCode)))
                .build();
    }
}