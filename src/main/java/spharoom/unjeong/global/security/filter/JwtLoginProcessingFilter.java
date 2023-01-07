package spharoom.unjeong.global.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import spharoom.unjeong.appointment.domain.entity.Admin;
import spharoom.unjeong.appointment.domain.repository.AdminRepository;
import spharoom.unjeong.global.common.SecurityCommonException;
import spharoom.unjeong.global.common.Utils;
import spharoom.unjeong.global.security.dto.LoginReqDto;
import spharoom.unjeong.global.security.token.UnjeongToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private AdminRepository adminRepository;

    public JwtLoginProcessingFilter(String processUrl) {
        super(new AntPathRequestMatcher(processUrl, "POST"));
    }

    public JwtLoginProcessingFilter addAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        return this;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("{} {} Login Request", request.getMethod(), request.getRequestURI());
        // request type 검증
        if (!isJson(request) || !request.getMethod().equalsIgnoreCase("POST")) {
            log.warn("Login request: Wrong Method or Type");
            throw new SecurityCommonException(2015, "로그인은 POST만 가능합니다.", HttpStatus.METHOD_NOT_ALLOWED);
        }

        LoginReqDto loginReqDto = Utils.objectMapper.readValue(request.getReader(), LoginReqDto.class);
        // request parameter 검증
        if (!StringUtils.hasText(loginReqDto.getLoginId()) || !StringUtils.hasText(loginReqDto.getPassword())) {
            log.warn("Login request: Null arguments");
            throw new SecurityCommonException(2016, "아이디와 비밀번호 모두 입력해야합니다.", HttpStatus.BAD_REQUEST);
        }

        // 로그인 검증
        Admin admin = adminRepository.findByLoginIdAndIsDeletedFalse(loginReqDto.getLoginId())
                .orElseThrow(() -> new SecurityCommonException(2017, "아이디가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED));

        String rawPassword = loginReqDto.getPassword();
        boolean result = Utils.passwordEncoder.matches(rawPassword, admin.getPassword());
        if (!result) throw new SecurityCommonException(2018, "비밀번호가 틀립니다.", HttpStatus.UNAUTHORIZED);

        // 인증토큰 발급
        return new UnjeongToken(admin);
    }

    private boolean isJson(HttpServletRequest request) {
        String headerType = request.getHeader("Content-Type").toLowerCase();
        return headerType.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
