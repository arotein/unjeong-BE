package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import spharoom.unjeong.global.common.CommonException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAppointmentDto {
    private String name;
    private String phone;

    public FindAppointmentDto validate() {
        if (!StringUtils.hasText(name) && !StringUtils.hasText(phone))
            throw new CommonException(30, "이름과 전화번호는 필수값입니다.");
        return this;
    }
}
