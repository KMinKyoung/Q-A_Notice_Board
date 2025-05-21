package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.minkyoung.qa_notice_board.domain.Role;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
}
