package spharoom.unjeong.appointment.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResDto;
import spharoom.unjeong.appointment.dto.response.AppointmentQueryResPreDto;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAppointmentServiceImpl implements AdminAppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentQueryResDto> findAllCustomerAppointment(AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = new ArrayList<>();
        Map<LocalDate, List<AppointmentQueryResPreDto>> preDtoMap = new TreeMap<>();

        List<Appointment> appointmentList = appointmentRepository.findAllAppointmentTwoWeeks(queryCondition);
        appointmentList.forEach(appointment -> {
            if (!preDtoMap.containsKey(appointment.getAppointmentDate())) {
                List<AppointmentQueryResPreDto> preDtoList = new ArrayList<>();
                preDtoList.add(AppointmentQueryResPreDto.of(appointment));
                preDtoMap.put(appointment.getAppointmentDate(), preDtoList);
            } else {
                preDtoMap.get(appointment.getAppointmentDate()).add(AppointmentQueryResPreDto.of(appointment));
            }
        });

        Iterator<LocalDate> iterator = preDtoMap.keySet().iterator();
        while (iterator.hasNext()) {
            LocalDate date = iterator.next();
            dtoList.add(new AppointmentQueryResDto(date, preDtoMap.get(date)));
        }
        return dtoList;
    }
}