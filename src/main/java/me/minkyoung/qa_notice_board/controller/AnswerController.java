package me.minkyoung.qa_notice_board.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.dto.AnswerRequestDto;
import me.minkyoung.qa_notice_board.dto.AnswerResponseDto;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.service.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    //답변 추가
    @PostMapping("/answers/{questionId}")
    public ResponseEntity addAnswer(@PathVariable Long questionId,@RequestBody AnswerRequestDto dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(answerService.addAnswer(dto, user, questionId));
    }

    //답변 수정
    @PutMapping("/answers/{answerId}")
    public ResponseEntity updateAnswer(@PathVariable Long answerId, @RequestBody AnswerRequestDto dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(answerService.updateAnswer(dto, user, answerId));
    }

    //답변 삭제
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId,
                                             @AuthenticationPrincipal User user) {
        answerService.deleteAnswer(answerId, user);
        return ResponseEntity.ok().build();
    }

    //답변 조회
    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<AnswerResponseDto>> getAllAnswer(@PathVariable Long questionId){
        return ResponseEntity.ok(answerService.getAllAnswers(questionId));
    }
}
