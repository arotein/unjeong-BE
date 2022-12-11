package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentType {
    CALL("전화상담예약"),
    VISIT("방문예약");

    public String description;
}
