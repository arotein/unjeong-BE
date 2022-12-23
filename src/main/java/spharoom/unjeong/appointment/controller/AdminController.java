package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.VacationReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.dto.response.RequiredContactCustomerResDto;
import spharoom.unjeong.appointment.dto.response.VacationResDto;
import spharoom.unjeong.appointment.service.admin.AdminService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${global.api.admin-base-path}")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/vacation")
    public CommonResponse findVacation() {
        List<VacationResDto> dtoList = adminService.findVacation();
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    @PostMapping("/vacation")
    public CommonResponse registerVacation(@RequestBody VacationReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("vacationDate", adminService.registerVacation(dto.checkValidation())))
                .build();
    }

    @DeleteMapping("/vacation")
    public CommonResponse deleteVacation(@RequestBody VacationReqDto dto) {
        return CommonResponse.builder()
                .data(Map.of("vacationDate", adminService.cancelVacation(dto.checkValidation())))
                .build();
    }

    @GetMapping("/appointment/list")
    public CommonResponse findAllCustomerAppointment(@Validated AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = adminService.findAllCustomerAppointment(queryCondition);
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    @GetMapping("/appointment/required-contact")
    public CommonResponse findRequiredContactCustomer() {
        List<RequiredContactCustomerResDto> dtoList = adminService.findRequiredContactCustomer();
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

}
