package spharoom.unjeong.appointment.service.customer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.domain.repository.CustomerRepository;
import spharoom.unjeong.appointment.domain.repository.VacationRepository;
import spharoom.unjeong.appointment.dto.request.AlterAppointmentReqDto;
import spharoom.unjeong.appointment.dto.request.AvailableCheckCondition;
import spharoom.unjeong.appointment.dto.request.FindAppointmentCondition;
import spharoom.unjeong.appointment.dto.request.RequestAppointmentReqDto;
import spharoom.unjeong.appointment.dto.response.AppointmentForCustomerResDto;
import spharoom.unjeong.appointment.dto.response.AvailableCheckResDto;
import spharoom.unjeong.global.common.CommonException;

import java.util.*;

/***
 * 스케줄러 : 매일 0시마다 WAITING이었던 전날 예약을 DONE으로 변경, CANCELED은 deleteData
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService { // 100~
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final VacationRepository vacationRepository;

    @Override
    public String requestAppointment(RequestAppointmentReqDto dto) {
        // 0. 예약가능 날짜인지 체크 - 휴가 검증
        boolean exist = vacationRepository.existsByVacationDate(dto.getAppointmentDate());
        if (exist) {
            throw new CommonException(108, "선택한 날에 예약할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        // 1. 첫 예약자의 경우 Customer 객체 생성
        Customer customer = customerRepository.findByNameAndPhone(dto.getName(), dto.getPhone()).orElseGet(() -> dto.toCustomerEntity());
        // 2. 요청자가 해당 날짜에 예약한게 있는지 확인 (예약자는 날짜별로 한 번의 예약만 가능) - 날짜 검증
        Appointment myAppointment = appointmentRepository.findLastAppointmentWithCustomer(customer.getName(), customer.getPhone(), dto.getAppointmentDate());
        if (myAppointment != null) {
            switch (myAppointment.getAppointmentState()) {
                case WAITING -> throw new CommonException(100, "선택한 날에 이미 예약을 하셨습니다.", HttpStatus.BAD_REQUEST);
                case CANCELED -> myAppointment.deleteCanceledAppointment();
            }
        }
        // 3. 요청 시간에 예약가능한지 확인 - 예약 검증
        Appointment appointmentByTime = appointmentRepository.findAppointmentByDateAndTime(dto.getAppointmentDate(), dto.getAppointmentTime());
        if (appointmentByTime != null) {
            throw new CommonException(107, "선택한 시간에 이미 예약이 존재하여 예약할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        // 4. 동시성 이슈 처리
        // 5. 카톡 안내메세지 외부 API 호출(비동기처리)
        return appointmentSafeSave(dto.toAppointmentEntity(customer)).getAppointmentCode();
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentForCustomerResDto findAllAppointmentByNameAndPhone(FindAppointmentCondition condition) {
        Customer customer = customerRepository.findByNameAndPhone(condition.getName(), condition.getPhone())
                .orElseThrow(() -> new CommonException(101, "예약한 이력이 없습니다.", HttpStatus.BAD_REQUEST));
        List<Appointment> appointmentList = appointmentRepository.findAllByCustomer(customer.getId());
        if (appointmentList.size() == 0) {
            throw new CommonException(102, "7일 이내에 예약한 이력이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        List<AppointmentForCustomerResDto.InnerDto> innerDtoList = appointmentList.stream().map(appointment -> AppointmentForCustomerResDto.InnerDto.of(appointment)).toList();
        return new AppointmentForCustomerResDto(condition.getName(), innerDtoList);
    }

    @Override
    public String alterAppointment(String appointmentCode, AlterAppointmentReqDto dto) {
        // 0. 예약가능 날짜인지 체크
        boolean exist = vacationRepository.existsByVacationDate(dto.getAlterDate());
        if (exist) {
            throw new CommonException(108, "선택한 날에 예약할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Appointment appointment = appointmentRepository.findByAppointmentCodeAndIsDeletedFalse(appointmentCode)
                .orElseThrow(() -> new CommonException(103, "예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        Customer customer = appointment.getCustomer();

        // 1. 예약날짜가 변경될 경우 -> 날짜 검증
        if (dto.getAlterDate() != null) {
            if (!dto.getAlterDate().isEqual(appointment.getAppointmentDate())) {
                // 해당 날짜에 요청자가 예약한게 있는지 확인
                Appointment alterDateAppointment = appointmentRepository.findLastAppointmentWithCustomer(customer.getName(), customer.getPhone(), dto.getAlterDate());
                if (alterDateAppointment != null) {
                    switch (alterDateAppointment.getAppointmentState()) {
                        case WAITING -> throw new CommonException(100, "선택한 날에 이미 예약을 하셨습니다.", HttpStatus.BAD_REQUEST);
                        case CANCELED -> alterDateAppointment.deleteCanceledAppointment(); // canceled예약 delete
                    }
                }
            }
            // 2. 해당시간에 예약가능한지 확인 -> 시간 검증
            Appointment appointmentByTime = appointmentRepository.findAppointmentByDateAndTime(dto.getAlterDate(), dto.getAlterTime());
            if (appointmentByTime != null && !Objects.equals(appointmentByTime.getCustomer().getId(), customer.getId())) {
                throw new CommonException(107, "선택한 시간에 이미 예약이 존재하여 예약할 수 없습니다.", HttpStatus.BAD_REQUEST);
            }
        }

        // 3. 2번 동시성이슈 처리 (추후 구현)
        // 4. 기존 예약 삭제 후 새 예약 생성
        Appointment copiedAppointment = appointment.copyAndAlterAppointment(dto, customer);
        return appointmentSafeSave(copiedAppointment.regenerateAppointmentCode()).getAppointmentCode();
    }

    @Override
    public String cancelAppointment(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCodeAndIsDeletedFalse(appointmentCode)
                .orElseThrow(() -> new CommonException(106, "예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
        appointment.toStateCanceled();
        return appointment.getAppointmentCode();
    }

    @Transactional(readOnly = true)
    @Override
    public AvailableCheckResDto availableTimeCheck(AvailableCheckCondition condition) {
        // 0. 예약가능 날짜인지 체크
        boolean exist = vacationRepository.existsByVacationDate(condition.getDate());
        if (exist) {
            return new AvailableCheckResDto(new ArrayList<>());
        }
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

    @Transactional(propagation = Propagation.MANDATORY)
    @SneakyThrows
    Appointment appointmentSafeSave(Appointment appointment) {
        Appointment savedAppointment = null;
        for (int k = 1; k <= 6; k++) {
            try {
                savedAppointment = appointmentRepository.save(appointment);
            } catch (DataIntegrityViolationException ex) {
                log.error("DataIntegrityViolationException 발생 --> AppointmentCode가 Unique하지 않게 생성됨({})", k);
                Thread.sleep(500);
                appointment.regenerateAppointmentCode();
            }
        }
        return savedAppointment;
    }
}
