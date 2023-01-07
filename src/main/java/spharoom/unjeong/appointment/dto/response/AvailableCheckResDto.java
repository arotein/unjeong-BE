package spharoom.unjeong.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableCheckResDto {
    private Integer time; // 시간
    private Boolean isAvailable; // 예약가능 여부
}
