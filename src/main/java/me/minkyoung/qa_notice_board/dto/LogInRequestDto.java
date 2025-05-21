package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequestDto {
    private String email;
    private String password;
}
