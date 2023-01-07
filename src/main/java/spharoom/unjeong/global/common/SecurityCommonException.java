package spharoom.unjeong.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class SecurityCommonException extends AuthenticationException {
    private Integer errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    public SecurityCommonException(Integer errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }

    public SecurityCommonException(Integer errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
    }
}
