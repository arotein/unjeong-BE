package spharoom.unjeong.appointment.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Vacation;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.domain.repository.VacationRepository;
import spharoom.unjeong.appointment.dto.request.AppointmentQueryCondition;
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
                .stream().map(vacation -> VacationResDto.of(vacation)).toList();
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

    @Transactional(readOnly = true)
    @Override
    public List<RequiredContactCustomerResDto> findRequiredContactCustomer() { // 날짜가 아니라 customer기준으로 묶자
        Map<CustomerMinDateDto, List<RequiredContactCustomerResPreDto>> preDtoMap = new TreeMap<>(Comparator.comparing(CustomerMinDateDto::getMinDateTime));

        appointmentRepository.findAllRequiredContactCustomer()
                .forEach(appointment -> {
                    CustomerMinDateDto customerDto = new CustomerMinDateDto(appointment);
                    if (!preDtoMap.containsKey(customerDto)) {
                        List<RequiredContactCustomerResPreDto> preDtoList = new ArrayList<>();
                        preDtoList.add(RequiredContactCustomerResPreDto.of(appointment));
                        preDtoMap.put(customerDto, preDtoList);
                    } else {
                        preDtoMap.get(customerDto).add(RequiredContactCustomerResPreDto.of(appointment));
                    }
                });

        List<RequiredContactCustomerResDto> dtoList = new ArrayList<>();

        Iterator<CustomerMinDateDto> iterator = preDtoMap.keySet().iterator();
        while (iterator.hasNext()) {
            CustomerMinDateDto customerDto = iterator.next();
            dtoList.add(new RequiredContactCustomerResDto(customerDto.getCustomer(), preDtoMap.get(customerDto)));
        }
        return dtoList;
    }
}