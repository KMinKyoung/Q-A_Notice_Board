package me.minkyoung.qa_notice_board.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("🔐 JwtAuthenticationFilter 실행됨: " + method + " " + requestURI);

        if (requestURI.startsWith("/auth")||(method.equals("GET")&&(requestURI.matches("^/questions(/.*)?$")
        ||requestURI.matches("^/api/questions(/.*)?$")||requestURI.matches("^/api/answers(/.*)?$")))) {
            filterChain.doFilter(request, response);
            return;
        }

        //Authrization 헤더에서 토큰 추출
        String token = resolveToken(request);
        System.out.println("📦 추출된 토큰: " + token);
        // 토큰이 유효하다면 SecurityContext에 인증 정보 저장
        if(token != null && jwtTokenProvider.validateToken(token)) {
            System.out.println("✅ 유효한 토큰 확인됨");
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("🎯 인증 객체 설정 완료: " + authentication.getName());
        } else {
            System.out.println("❌ 유효하지 않은 토큰 또는 없음");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
