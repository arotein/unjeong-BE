package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import spharoom.unjeong.global.common.CommonException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AppointmentType { // 상담 종류
    CALL("전화상담"),
    VISIT("방문상담");

    public String description;

    public AppointmentType valueOfString(String stringValue) {
        return Arrays.asList(AppointmentType.values()).stream()
                .filter(value -> value.name().equalsIgnoreCase(stringValue))
                .filter(value -> value.getDescription().equals(stringValue))
                .findAny()
                .orElseThrow(() -> new CommonException(400, "존재하지 않는 상담종류입니다.", HttpStatus.BAD_REQUEST));

    }
}
