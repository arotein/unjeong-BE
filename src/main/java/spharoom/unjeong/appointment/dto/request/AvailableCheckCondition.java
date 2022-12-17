package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import spharoom.unjeong.global.common.CommonException;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCheckCondition {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public AvailableCheckCondition checkValidation() {
        LocalDate nowDate = LocalDate.now();
        LocalDate nextWeekDate = nowDate.plusDays(7);

        if (date.isBefore(nowDate) || date.isAfter(nextWeekDate)) {
            throw new CommonException(9, "7일 이내만 조회 가능합니다.");
        }
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new CommonException(10, "일요일은 예약이 불가능합니다.");
        }
        return this;
    }
}
