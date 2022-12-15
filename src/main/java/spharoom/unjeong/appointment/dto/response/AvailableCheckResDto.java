package spharoom.unjeong.appointment.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AvailableCheckResDto {
    private List<Integer> availableTime;
}
