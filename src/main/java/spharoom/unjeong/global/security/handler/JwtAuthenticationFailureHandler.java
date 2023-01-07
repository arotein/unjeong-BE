package spharoom.unjeong.global.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import spharoom.unjeong.appointment.service.admin.AdminService;
import spharoom.unjeong.global.common.CommonResponse;
import spharoom.unjeong.global.common.SecurityCommonException;
import spharoom.unjeong.global.common.Utils;
import spharoom.unjeong.global.enumeration.AccessResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static spharoom.unjeong.global.common.Utils.objectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final AdminService adminService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SecurityCommonException securityException = (SecurityCommonException) exception;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(),
                CommonResponse.builder()
                        .errorCode(securityException.getErrorCode())
                        .errorMessage(securityException.getErrorMessage())
                        .build());

        adminService.registerLoginLog(Utils.IpAddressExtractor(request), AccessResult.DENIED);
    }
}
