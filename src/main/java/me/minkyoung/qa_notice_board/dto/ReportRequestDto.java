package me.minkyoung.qa_notice_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.minkyoung.qa_notice_board.domain.ReportType;

@Getter
@NoArgsConstructor
public class ReportRequestDto {
    private ReportType type;
    private Long targetId;
    private String reason;
}
