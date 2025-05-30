package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minkyoung.qa_notice_board.domain.ReportType;
import me.minkyoung.qa_notice_board.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private Long id;
    private ReportType type;
    private Long targetId;
    private String reason;
    private User reporter;
    private LocalDateTime createdAt;
}
