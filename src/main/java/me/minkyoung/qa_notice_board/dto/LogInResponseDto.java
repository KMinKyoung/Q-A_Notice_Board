package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogInResponseDto {
    private String accessToken;
    private String refreshToken;

}
