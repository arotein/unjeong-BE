package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import spharoom.unjeong.appointment.domain.entity.Vacation;
import spharoom.unjeong.global.common.CommonException;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VacationReqDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate vacationDate;

    public Vacation toEntity() {
        return Vacation.builder().vacationDate(vacationDate).build();
    }

    public VacationReqDto checkValidation() {
        if (vacationDate == null) {
            throw new CommonException(20, "vacationDate는 null일 수 없습니다.");
        }
        if (vacationDate.isBefore(LocalDate.now())) {
            throw new CommonException(21, "이미 지난 날짜는 선택할 수 없습니다.");
        }
        return this;
    }
}
