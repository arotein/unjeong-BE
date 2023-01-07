package spharoom.unjeong.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Utils {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
}
