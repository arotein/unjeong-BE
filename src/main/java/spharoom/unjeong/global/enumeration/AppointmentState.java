package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentState {
    WAITING("상담대기중"),

    ALTERED("예약변경됨"),
    CANCELED("예약취소됨"),
    DONE("상담완료");

    public String description;
}
