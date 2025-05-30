package me.minkyoung.qa_notice_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {
    private String content;
   // private Long questionId; //질문에 대한 답변
}
