package spharoom.unjeong.global.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import spharoom.unjeong.global.common.CommonException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StaticImg {
    STORE_1("store_1.jpg"),
    STORE_2("store_2.jpg"),
    STORE_3("store_3.jpg"),
    STORE_4("store_4.jpg");

    public String imgName;

    public static StaticImg findByString(String imgName) {
        return Arrays.asList(StaticImg.values()).stream()
                .filter(img -> StringUtils.containsIgnoreCase(img.toString(), imgName))
                .filter(img -> StringUtils.containsIgnoreCase(img.getImgName(), imgName))
                .findFirst()
                .orElseThrow(() -> new CommonException(50, "이미지가 존재하지 않습니다.", HttpStatus.BAD_REQUEST));
    }
}
