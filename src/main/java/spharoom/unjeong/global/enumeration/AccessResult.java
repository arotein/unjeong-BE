package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessResult {
    DENIED("거부됨"),
    CONFIRMED("허가됨");

    public String description;
}
