package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private String content;
    private String writerNickname;
    private Long questionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AnswerResponseDto() {

    }
 }
