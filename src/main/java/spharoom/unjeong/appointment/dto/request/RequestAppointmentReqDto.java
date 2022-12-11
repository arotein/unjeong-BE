package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.global.common.CommonException;
import spharoom.unjeong.global.enumeration.AppointmentType;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAppointmentReqDto {
    private String name;
    private String phone;
    @NotNull
    private AppointmentType appointmentType;
    private Integer numberOfPeople;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    private Integer appointmentHour;

    public Appointment toEntity() {
        validationCheck();
        return Appointment.builder()
                .name(name)
                .phone(phone)
                .appointmentType(appointmentType)
                .numberOfPeople(numberOfPeople)
                .appointmentDateTime(appointmentDate.atStartOfDay().plusHours(appointmentHour))
                .build();
    }

    public void validationCheck() {
        LocalDate nowDate = LocalDate.now();
        LocalDate nextWeekDate = nowDate.plusDays(7);

        LocalTime nowTime = LocalTime.now();

        if (!name.matches("^[가-힣]+$")) {
            throw new CommonException(6, "이름은 한글만 가능합니다.");
        }
        if (!phone.matches("^01\\d-\\d{3,4}-\\d{4}$")) {
            throw new CommonException(7, "휴대폰 번호는 000-0000-0000의 형태만 가능합니다.");
        }
        if (numberOfPeople <= 0 || numberOfPeople >= 10) {
            throw new CommonException(8, "신청 인원수는 1~9명만 가능합니다.");
        }
        if (appointmentDate.isBefore(nowDate) || appointmentDate.isAfter(nextWeekDate)) {
            throw new CommonException(9, "7일 이내만 예약이 가능합니다.");
        }
        if (appointmentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new CommonException(10, "일요일은 예약이 불가능합니다.");
        }
        if (appointmentHour < 11 || appointmentHour > 19) {
            throw new CommonException(11, "예약 가능 시간은 11~19시입니다.");
        }
        if (appointmentHour < nowTime.getHour()) {
            throw new CommonException(12, "지난 시간은 예약할 수 없습니다.");
        }
        if (appointmentHour == nowTime.getHour()) {
            throw new CommonException(13, "1시간 후부터 예약 가능합니다.");
        }
    }
}
