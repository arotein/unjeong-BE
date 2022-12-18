package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentType { // 상담 종류
    CALL("전화상담"),
    VISIT("방문상담");

    public String description;
}
