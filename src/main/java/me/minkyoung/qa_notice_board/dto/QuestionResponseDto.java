package me.minkyoung.qa_notice_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.minkyoung.qa_notice_board.entity.Question;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;

    public QuestionResponseDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
        this.writerNickname = question.getUser().getNickname();
        this.email = question.getUser().getEmail();
    }
}
