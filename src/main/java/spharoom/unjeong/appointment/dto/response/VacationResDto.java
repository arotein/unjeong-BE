package spharoom.unjeong.appointment.dto.response;

import lombok.*;
import spharoom.unjeong.appointment.domain.entity.Vacation;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacationResDto {
    private Integer index;
    private LocalDate vacationDate;

    public static VacationResDto of(Vacation vacation) {
        return VacationResDto.builder().vacationDate(vacation.getVacationDate()).build();
    }
}
