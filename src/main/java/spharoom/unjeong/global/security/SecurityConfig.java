package spharoom.unjeong.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;
import spharoom.unjeong.appointment.domain.repository.AdminRepository;
import spharoom.unjeong.global.security.handler.JwtAuthenticationFailureHandler;
import spharoom.unjeong.global.security.handler.JwtAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String LOGIN_PROCESSING_URL = "/api/login";
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final CorsFilter corsFilter;
    private final AdminRepository adminRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
        http.logout().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.headers().frameOptions().disable();
        http.rememberMe().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
//                .antMatchers("/api/admin/**").authenticated()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/hello/**").permitAll()
                .antMatchers("/api/static/**").permitAll()
                .antMatchers("/api/customer/**").permitAll();

        http.addFilterBefore(corsFilter, SecurityContextPersistenceFilter.class);

        // request
//        http.addFilterAfter(new JwtRequestProcessingFilter("/api/admin/*"), SecurityContextPersistenceFilter.class);

        // login
        applyJwtConfigurer(http);
    }

    private void applyJwtConfigurer(HttpSecurity http) throws Exception {
        http.apply(new JwtLoginConfigurer<>(LOGIN_PROCESSING_URL))
                .loginSuccessHandler(jwtAuthenticationSuccessHandler)
                .loginFailureHandler(jwtAuthenticationFailureHandler)
                .adminRepository(adminRepository);
    }
}
