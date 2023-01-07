package spharoom.unjeong.global.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import spharoom.unjeong.appointment.domain.entity.Admin;
import spharoom.unjeong.appointment.service.admin.AdminService;
import spharoom.unjeong.global.common.CommonResponse;
import spharoom.unjeong.global.common.Utils;
import spharoom.unjeong.global.enumeration.AccessResult;
import spharoom.unjeong.global.security.token.UnjeongToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static spharoom.unjeong.global.common.Utils.objectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AdminService adminService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // HTTP 헤더로 토큰 응답
        UnjeongToken token = (UnjeongToken) authentication;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", token.generateJwt());
        objectMapper.writeValue(response.getWriter(), CommonResponse.builder().data(Map.of("result", true)).build());

        // 로그인 시간 갱신
        Admin admin = (Admin) token.getPrincipal();
        adminService.updateLoginDateTime(admin.getId());

        adminService.registerLoginLog(Utils.IpAddressExtractor(request), AccessResult.CONFIRMED);
    }
}
