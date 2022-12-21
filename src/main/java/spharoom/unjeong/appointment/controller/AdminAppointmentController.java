package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.service.admin.AdminAppointmentService;
import spharoom.unjeong.global.common.CommonResponse;

import java.util.List;

@RestController
@RequestMapping("${global.api.admin-base-path}/appointment")
@RequiredArgsConstructor
public class AdminAppointmentController {
    private final AdminAppointmentService appointmentService;

    @GetMapping("/list")
    public CommonResponse findAllCustomerAppointment(@Validated AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = appointmentService.findAllCustomerAppointment(queryCondition);
        dtoList.forEach(dto -> dto.setIndex(dtoList.indexOf(dto)));
        return CommonResponse.builder()
                .data(dtoList)
                .build();
    }
}
