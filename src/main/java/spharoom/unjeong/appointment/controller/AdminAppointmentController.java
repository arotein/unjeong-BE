package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.VacationReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.dto.response.RequiredContactCustomerResDto;
import spharoom.unjeong.appointment.service.admin.AdminAppointmentService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${global.api.admin-base-path}/appointment")
@RequiredArgsConstructor
public class AdminAppointmentController {
    private final AdminAppointmentService appointmentService;

    @PostMapping("/vacation")
    public CommonResponse registerVacation(@RequestBody VacationReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("vacationDate", appointmentService.registerVacation(dto.checkValidation())))
                .build();
    }

    @DeleteMapping("/vacation")
    public CommonResponse deleteVacation(@RequestBody VacationReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("vacationDate", appointmentService.cancelVacation(dto.checkValidation())))
                .build();
    }

    @GetMapping("/list")
    public CommonResponse findAllCustomerAppointment(@Validated AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = appointmentService.findAllCustomerAppointment(queryCondition);
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    @GetMapping("/required-contact")
    public CommonResponse findRequiredContactCustomer() {
        List<RequiredContactCustomerResDto> dtoList = appointmentService.findRequiredContactCustomer();
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

}
