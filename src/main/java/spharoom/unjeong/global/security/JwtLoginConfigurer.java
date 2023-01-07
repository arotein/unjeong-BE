package spharoom.unjeong.global.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import spharoom.unjeong.appointment.domain.repository.AdminRepository;
import spharoom.unjeong.global.security.filter.JwtLoginProcessingFilter;
import spharoom.unjeong.global.security.handler.JwtAuthenticationFailureHandler;
import spharoom.unjeong.global.security.handler.JwtAuthenticationSuccessHandler;

public class JwtLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, JwtLoginConfigurer<H>, JwtLoginProcessingFilter> {

    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private AdminRepository adminRepository;
    private final String LOGIN_PROCESSING_METHOD = "POST";

    public JwtLoginConfigurer(String loginProcessingUrl) {
        super(new JwtLoginProcessingFilter(loginProcessingUrl), loginProcessingUrl);
    }

    @Override
    public void configure(H http) throws Exception {
        JwtLoginProcessingFilter jwtFilter = getAuthenticationFilter();
        jwtFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
        jwtFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);
        jwtFilter.addAdminRepository(adminRepository);

        SessionAuthenticationStrategy sessionStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionStrategy != null) {
            jwtFilter.setSessionAuthenticationStrategy(sessionStrategy);
        }

        http.setSharedObject(JwtLoginProcessingFilter.class, jwtFilter);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, LOGIN_PROCESSING_METHOD);
    }

    public JwtLoginConfigurer<H> loginSuccessHandler(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
        return this;
    }

    public JwtLoginConfigurer<H> loginFailureHandler(JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler) {
        this.jwtAuthenticationFailureHandler = jwtAuthenticationFailureHandler;
        return this;
    }

    public JwtLoginConfigurer<H> adminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        return this;
    }
}
