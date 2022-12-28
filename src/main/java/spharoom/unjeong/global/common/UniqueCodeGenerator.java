package spharoom.unjeong.global.common;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UniqueCodeGenerator {
    public static String generateCode() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String randomStr = RandomStringUtils.randomAlphanumeric(4);
        return String.format("%s-%s", time, randomStr);
    }
}
