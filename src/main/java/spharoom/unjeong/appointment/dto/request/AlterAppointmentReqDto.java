package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import spharoom.unjeong.global.common.CommonException;
import spharoom.unjeong.global.enumeration.AppointmentType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlterAppointmentReqDto {
    private AppointmentType alterAppointmentType;
    private Integer alterNumberOfPeople;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate alterDate; // date와 hour는 같이 입력받아야 됨
    private Integer alterHour;

    public LocalTime getAlterTime() {
        return alterHour == null ? null : LocalTime.of(alterHour, 0);
    }

    public AlterAppointmentReqDto checkValidation() {
        LocalDate nowDate = LocalDate.now();
        LocalDate nextWeekDate = nowDate.plusDays(7);
        LocalTime nowTime = LocalTime.now();

        if (alterAppointmentType == null && alterNumberOfPeople == null && alterDate == null && alterHour == null) {
            throw new CommonException(14, "모든 파라미터가 null입니다.");
        }

        if (alterNumberOfPeople != null && (alterNumberOfPeople <= 0 || alterNumberOfPeople >= 10)) {
            throw new CommonException(8, "신청 인원수는 1~9명만 가능합니다.");
        }

        if (alterDate != null && alterHour != null) {
            if (alterDate.isBefore(nowDate) || alterDate.isAfter(nextWeekDate)) {
                throw new CommonException(9, "7일 이내만 예약이 가능합니다.");
            }
            if (alterDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                throw new CommonException(10, "일요일은 예약이 불가능합니다.");
            }
            if (alterHour < 11 || alterHour > 19) {
                throw new CommonException(11, "예약 가능 시간은 11~19시입니다.");
            }
            if (alterDate.isEqual(nowDate) && alterHour < nowTime.getHour()) {
                throw new CommonException(12, "지난 시간은 예약할 수 없습니다.");
            }
            if (alterDate.isEqual(nowDate) && alterHour == nowTime.getHour()) {
                throw new CommonException(13, String.format("%d시 이후 시간만 예약 가능합니다.", nowTime.getHour() + 1));
            }
        }

        if ((alterDate == null && alterHour != null) || (alterDate != null && alterHour == null)) {
            throw new CommonException(15, "날짜와 시간 모두 선택해야 합니다.");
        }
        return this;
    }
}
