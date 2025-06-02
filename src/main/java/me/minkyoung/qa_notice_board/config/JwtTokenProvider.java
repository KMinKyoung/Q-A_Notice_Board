package me.minkyoung.qa_notice_board.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import me.minkyoung.qa_notice_board.service.UserDetailService;
import org.springframework.beans.factory.annotation.Value;
import me.minkyoung.qa_notice_board.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailService userDetailService;
    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenValidity = 1000L*60*30; //30분
    private final long refreshTokenValidity = 1000L*60*60*24*7; //7일

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //access token 발급
    public String createAccessToken(User user) {
       return createToken(user, accessTokenValidity);

    }

    //refresh token 발급
    public String createRefreshToken(User user){
        return createToken(user, refreshTokenValidity);
    }

    //실제 토큰 생성 로직
    private String createToken(User user, long validity) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(user.getEmail()) //사용자 식별값(email)
                .claim("auth",user.getRole().name()) //권한
                .setIssuedAt(now) //생성일
                .setExpiration(expiry) //만료일
                .signWith(SignatureAlgorithm.HS512, secretKey) //해시암호로 서명
                .compact();
    }

    public boolean validateToken(String token) { //토큰 검증(서버에서 만든건지, 만료되지 않은건지)
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) //서명
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        //권한 꺼내기
        String auth = claims.get("auth", String.class); //auth인 String을 꺼내옴
        if(auth==null){
            throw new RuntimeException("권한 정보가 없습니다.");
        }

        String email =claims.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다:"+email));

       // UserDetails userDetails = userDetailService.loadUserByUsername(claims.getSubject());

        //권한 리스트로 변환
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auth.startsWith("ROLE_")?auth : "ROLE_"+auth);

        //Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(user,"",authorities);
    }
}
