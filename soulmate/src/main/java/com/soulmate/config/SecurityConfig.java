package com.soulmate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                //페이지별 권한 추가
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                //사용자 정의 로그인
                .formLogin(form -> form.loginPage("/login").permitAll().loginProcessingUrl("/api/login").defaultSuccessUrl("/"));
        return http.build();
    }
}