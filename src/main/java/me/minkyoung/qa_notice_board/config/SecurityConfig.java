package me.minkyoung.qa_notice_board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() //로그인, 회원가입 허용
                        .requestMatchers(HttpMethod.GET,"/api/**").permitAll() //목록/상세 비회원 가능
                        .requestMatchers(HttpMethod.GET, "/questions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()//비회원 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/reports").hasRole("USER") //유저만
                        .requestMatchers(HttpMethod.GET, "/api/reports").hasRole("ADMIN") //관리자만
                        .requestMatchers(HttpMethod.DELETE, "/api/reports/**").hasRole("ADMIN") //관리자만
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
