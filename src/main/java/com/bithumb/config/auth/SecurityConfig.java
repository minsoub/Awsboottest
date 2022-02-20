package com.bithumb.config.auth;

import com.bithumb.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity   // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected  void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()  // URL 별 권환 관리 설정 옵션 시작
                .antMatchers("/", "/css/**", "/images/**", "js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()  // 설정된 값들 이외 나머지 URL에 대해서 인증된 사용자만 허용
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login()  // OAuth2 로그인 기능에 대한 설정 진입점
                    .userInfoEndpoint().userService(customOAuth2UserService);  // 소셜 로그인 성공 시 후속 조치를 진행할 서비스
    }
}
