package spharoom.unjeong.appointment.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.domain.repository.CustomerRepository;
import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;
import spharoom.unjeong.global.common.CommonException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/***
 * 스케줄러 : 매일 0시마다 WAITING이었던 전날 예약을 DONE으로 변경, CANCELED은 deleteData
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerAppointmentServiceImpl implements CustomerAppointmentService { // 100~
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Long requestAppointment(RequestAppointmentReqDto dto) {
        Customer customer = customerRepository.findByNameAndPhone(dto.getName(), dto.getPhone()).orElseGet(() -> dto.toCustomerEntity());
        // 1. 중복예약 체크(하루에 한 팀만)
        Boolean isExist = appointmentRepository.existsByCustomer_NameAndCustomer_PhoneAndAppointmentDateAndIsDeletedFalse(customer.getName(), customer.getPhone(), dto.getAppointmentDate());
        if (isExist) {
            throw new CommonException(100, "선택한 날에 이미 예약이 존재합니다.", HttpStatus.BAD_REQUEST);
        }
        // 2. 동시성 이슈 처리
        // 3. 카톡 안내메세지 외부 API 호출(비동기처리)
        return appointmentRepository.save(dto.toAppointmentEntity(customer)).getId();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentResDto> findAllAppointmentByNameAndPhone(FindAppointmentCondition condition) {
        LocalDate today = LocalDate.now();
        Customer customer = customerRepository.findByNameAndPhone(condition.getName(), condition.getPhone())
                .orElseThrow(() -> new CommonException(101, "7일 이내에 예약한 이력이 없습니다.", HttpStatus.BAD_REQUEST));
        List<Appointment> appointmentList = appointmentRepository.findAllByCustomer_IdAndAppointmentDateBetweenAndIsDeletedFalse(customer.getId(), today, today.plusDays(7));
        if (appointmentList.size() == 0) {
            throw new CommonException(102, "7일 이내에 예약한 이력이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return appointmentList.stream().map(appointment -> AppointmentResDto.of(appointment)).toList();
    }

    @Override
    public Long alterAppointment(Long appointmentId, AlterAppointmentReqDto dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new CommonException(103, "예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        Customer customer = appointment.getCustomer();

        // 1. 검색된 customer가 해당 날짜에 예약한게 있는지 확인
        Boolean isExistDate = appointmentRepository.existsByCustomer_IdAndAppointmentDateAndIsDeletedFalse(customer.getId(), dto.getAlterDate());
        if (isExistDate) {
            throw new CommonException(104, "선택한 날에 이미 예약이 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. alterDate에 본인이 예약한게 없으면 alterTime에 누가 예약한게 없는지 확인
        Boolean isExistTime = appointmentRepository.existsByAppointmentDateAndAndAppointmentTimeAndIsDeletedFalse(dto.getAlterDate(), dto.getAlterTime());
        if (isExistTime) {
            throw new CommonException(105, "선택한 시간에 예약할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 3. 2번 동시성이슈 처리 (추후 구현)
        Appointment copiedAppointment = appointment.copyAndAlterAppointment(dto, customer);
        return appointmentRepository.save(copiedAppointment).getId();
    }

    @Override
    public Long cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new CommonException(106, "예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        appointment.toStateCanceled();
        return appointment.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition) {
        List<Appointment> appointmentList = appointmentRepository.findAllByAppointmentDateAndIsDeletedFalse(condition.getDate());
        Map<Integer, Integer> availableTimeMap = generateAvailableTimeMap();
        appointmentList.forEach(appointment -> {
            int hour = appointment.getAppointmentTime().getHour();
            availableTimeMap.remove(hour);
        });
        return new AvailableCheckResDto(availableTimeMap.values().stream().toList());
    }

    private Map<Integer, Integer> generateAvailableTimeMap() {
        Map<Integer, Integer> timeMap = new TreeMap<>();
        for (int k = 11; k <= 19; k++) {
            timeMap.put(k, k);
        }
        return timeMap;
    }
}
