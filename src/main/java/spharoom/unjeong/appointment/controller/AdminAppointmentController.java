package spharoom.unjeong.appointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spharoom.unjeong.appointment.service.admin.AdminAppointmentService;

@RestController
@RequestMapping("${global.api.admin-base-path}/appointment")
@RequiredArgsConstructor
public class AdminAppointmentController {
    private final AdminAppointmentService appointmentService;
}
