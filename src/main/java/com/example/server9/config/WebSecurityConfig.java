package com.example.server9.config;

import com.example.server9.token.JwtAuthenticationFilter;
import com.example.server9.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // SecurityFilterChain Bean 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // h2 콘솔 사용
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // 세션 사용 안함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                // URL 관리
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/join", "/login", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                // CORS 설정
                .cors(AbstractHttpConfigurer::disable) // CORS 정책 활성화(사용하려면 설정할 것)

                // CSRF 설정
                .csrf(AbstractHttpConfigurer::disable) // 필요에 따라 CSRF 비활성화

                // 인증 및 권한 설정
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/join/**").permitAll() // /api/** 경로에 대한 접근을 모두 허용
//                        .anyRequest().authenticated()          // 그 외 모든 요청은 인증 필요
//                )

                // JwtAuthenticationFilter를 먼저 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}