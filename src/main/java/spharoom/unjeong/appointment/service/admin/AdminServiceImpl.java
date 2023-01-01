package spharoom.unjeong.appointment.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Vacation;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.domain.repository.VacationRepository;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
import spharoom.unjeong.appointment.dto.request.TodayAppointmentByTypeCondition;
import spharoom.unjeong.appointment.dto.request.VacationReqDto;
import spharoom.unjeong.appointment.dto.response.*;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AppointmentRepository appointmentRepository;
    private final VacationRepository vacationRepository;

    @Override
    public List<VacationResDto> findVacation() {
        LocalDate nowDate = LocalDate.now();
        return vacationRepository.findAllByVacationDateIsGreaterThanEqualOrderByVacationDateAsc(nowDate)
                .stream().map(VacationResDto::of).toList();
    }

    @Override
    public LocalDate registerVacation(VacationReqDto dto) {
        Optional<Vacation> vacation = vacationRepository.findByVacationDate(dto.getVacationDate());
        if (vacation.isPresent()) {
            return vacation.get().getVacationDate();
        }
        return vacationRepository.save(dto.toEntity()).getVacationDate();
    }

    @Override
    public LocalDate cancelVacation(VacationReqDto dto) {
        vacationRepository.deleteByVacationDate(dto.getVacationDate());
        return dto.getVacationDate();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentQueryResDto> findAllCustomerAppointment(AppointmentQueryCondition queryCondition) {
        List<AppointmentQueryResDto> dtoList = new ArrayList<>();
        Map<LocalDate, List<AppointmentForAdminDto>> preDtoMap = new TreeMap<>();

        List<Appointment> appointmentList = appointmentRepository.findAllAppointmentTwoWeeks(queryCondition);
        appointmentList.forEach(appointment -> {
            if (!preDtoMap.containsKey(appointment.getAppointmentDate())) {
                List<AppointmentForAdminDto> preDtoList = new ArrayList<>();
                preDtoList.add(AppointmentForAdminDto.of(appointment));
                preDtoMap.put(appointment.getAppointmentDate(), preDtoList);
            } else {
                preDtoMap.get(appointment.getAppointmentDate()).add(AppointmentForAdminDto.of(appointment));
            }
        });

        for (LocalDate date : preDtoMap.keySet()) dtoList.add(new AppointmentQueryResDto(date, preDtoMap.get(date)));

        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequiredContactCustomerResDto> findRequiredContactCustomer() { // 날짜가 아니라 customer기준으로 묶자
        List<RequiredContactCustomerResDto> dtoList = new ArrayList<>();

        appointmentRepository.findAllRequiredContactCustomer()
                .forEach(appointment -> {
                    int addCount = 0;
                    for (RequiredContactCustomerResDto dto : dtoList) {
                        if (dto.samePeopleCheck(appointment.getCustomer())) {
                            dto.addToAppointmentList(appointment);
                            addCount++;
                            break;
                        }
                    }
                    if (addCount == 0) {
                        dtoList.add(new RequiredContactCustomerResDto(appointment));
                    }
                });

        dtoList.forEach(RequiredContactCustomerResDto::addIndex);
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public TodayAppointmentByTypeDto findTodayAppointmentByType(TodayAppointmentByTypeCondition condition) {
        List<AppointmentForAdminDto> innerDtoList = appointmentRepository.findAllByAppointmentDateAndIsDeletedFalse(LocalDate.now()).stream()
                .filter(appointment -> appointment.getAppointmentType() == condition.getAppointmentType())
                .map(AppointmentForAdminDto::of)
                .toList();
        return new TodayAppointmentByTypeDto(condition.getAppointmentType(), innerDtoList);
    }
}