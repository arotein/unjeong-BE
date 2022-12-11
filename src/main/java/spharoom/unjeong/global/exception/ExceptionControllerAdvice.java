package spharoom.unjeong.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spharoom.unjeong.global.common.CommonException;
import spharoom.unjeong.global.common.CommonResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
//    @ExceptionHandler
//    public ResponseEntity runtimeExHandle(RuntimeException ex) {
//        log.error("Exception Name = {}, Code = 1, Message = {}", ex.getClass().getName(), ex.getMessage());
//        return ResponseEntity.ok()
//                .body(CommonResponse.builder().errorCode(1).build());
//    }

    @ExceptionHandler
    public ResponseEntity methodNotAllowedExHandle(HttpRequestMethodNotSupportedException ex) {
        log.warn("Exception Name = {}, Code = 2, Message = {}", ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CommonResponse.builder().errorCode(2).build());
    }

    @ExceptionHandler
    public ResponseEntity illegalArgumentExHandle(IllegalArgumentException ex) {
        log.warn("Exception Name = {}, Code = 3, Message = {}", ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder().errorCode(3).build());
    }

    @ExceptionHandler
    public ResponseEntity constraintViolationExHandle(ConstraintViolationException ex) {
        log.warn("Exception Name = {}, Code = 4, Message = {}", ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder().errorCode(4).build());
    }

    @ExceptionHandler
    public ResponseEntity httpMessageNotReadableExHandle(HttpMessageNotReadableException ex) {
        log.warn("Exception Name = {}, Code = 5, Message = {}", ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder().errorCode(5).build());
    }

    @ExceptionHandler
    public ResponseEntity commonExHandle(CommonException ex) {
        log.warn("Exception Name = {}, Code = {}, Message = {}", ex.getClass().getName(), ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
                .body(CommonResponse.builder().errorCode(ex.getErrorCode()).build());
    }
}
