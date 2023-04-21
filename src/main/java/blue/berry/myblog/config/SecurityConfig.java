package blue.berry.myblog.config;

import blue.berry.myblog.core.auth.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable();

        // 2. iframe option 해제(시큐리티 h2-console 접속 허용을 위해서)
        http.headers().frameOptions().disable();

        // 2. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    log.debug("디버그 : 로그인 성공");

                    // view에서 사용하기 위한 세션 생성
                    MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                    HttpSession session = request.getSession();
                    session.setAttribute("sessionUser", myUserDetails.getUser());

                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {
                    log.debug("디버그 : 로그인 실패 : " + exception.getMessage());
                    response.sendRedirect("/loginForm");
                });

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
                authorize -> authorize.antMatchers("/s/**").authenticated()
                        .anyRequest().permitAll()
        );

        return http.build();
    }
}
