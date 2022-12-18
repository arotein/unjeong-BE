package spharoom.unjeong.global.common;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UniqueCodeGenerator {
    public static String generateCode() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = RandomStringUtils.randomAlphanumeric(3);
        return String.format("%s-%s", time, randomStr);
    }
}
