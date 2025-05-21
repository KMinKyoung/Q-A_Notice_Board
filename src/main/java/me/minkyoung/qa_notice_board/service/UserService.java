package me.minkyoung.qa_notice_board.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.config.JwtTokenProvider;
import me.minkyoung.qa_notice_board.domain.Role;
import me.minkyoung.qa_notice_board.dto.LogInRequestDto;
import me.minkyoung.qa_notice_board.dto.LogInResponseDto;
import me.minkyoung.qa_notice_board.dto.SignUpRequestDto;
import me.minkyoung.qa_notice_board.dto.SignUpResponse;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    public SignUpResponse signup(SignUpRequestDto requestDto){
        // 이메일 중복 검사
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }
        //비밀번호 암호화
        String encodedPassword=passwordEncoder.encode(requestDto.getPassword());

        //user 엔터티로 생성
        User user = User.builder()
                        .email(requestDto.getEmail())
                        .password(encodedPassword) //암호화 후 저장
                        .nickname(requestDto.getNickname())
                        .role(Role.ROLE_USER)
                        .build();

        User savedUser = userRepository.save(user);
        return new SignUpResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getNickname(),
                savedUser.getRole().name()
        );
    }

    //로그인
    public LogInResponseDto login(LogInRequestDto requestDto){
    //이메일로 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다"));

        //비밀번호 매칭
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        //LogInResponseDto에 담아 리턴
        return new LogInResponseDto(accessToken, refreshToken);
    }
    /*
    //사용자 정보 조회
    public getUserInfo(String email){

    }*/
}
