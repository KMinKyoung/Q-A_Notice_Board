package me.minkyoung.qa_notice_board.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.dto.LogInRequestDto;
import me.minkyoung.qa_notice_board.dto.LogInResponseDto;
import me.minkyoung.qa_notice_board.dto.SignUpRequestDto;
import me.minkyoung.qa_notice_board.dto.SignUpResponse;
import me.minkyoung.qa_notice_board.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;




@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        SignUpResponse response = userService.signup(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody @Valid LogInRequestDto requestDto){
        LogInResponseDto response = userService.login(requestDto);
        return ResponseEntity.ok(response);
    }
}
