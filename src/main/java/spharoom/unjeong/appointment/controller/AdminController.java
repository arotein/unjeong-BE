package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.TodayAppointmentByTypeCondition;
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

    // 2주치 예약 목록 조회
    @GetMapping("/appointment/list")
    public CommonResponse findAllCustomerAppointment(@Validated AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = adminService.findAllCustomerAppointment(queryCondition);
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }

    // 연락해야 될 사용자 조회 ... 예약과 휴가가 겹칠 때
    @GetMapping("/appointment/required-contact")
    public CommonResponse findRequiredContactCustomer() {
        List<RequiredContactCustomerResDto> dtoList = adminService.findRequiredContactCustomer();
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }


    // 오늘의 전화상담 예약자 조회 ... CALL, VISIT
    @GetMapping("/appointment/today")
    public CommonResponse findTodayAppointmentByType(@Validated TodayAppointmentByTypeCondition condition) {
        return CommonResponse.builder()
                .data(adminService.findTodayAppointmentByType(condition))
                .build();
    }
}
