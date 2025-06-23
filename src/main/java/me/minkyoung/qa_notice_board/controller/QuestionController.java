package me.minkyoung.qa_notice_board.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.dto.QuestionRequestDto;
import me.minkyoung.qa_notice_board.dto.QuestionResponseDto;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    //질문 등록(로그인한 사용자만)
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/questions")
    public ResponseEntity addQuestion(@RequestBody QuestionRequestDto requestDto, @AuthenticationPrincipal User user) {
        QuestionResponseDto add= questionService.createQuestion(requestDto, user);
        return ResponseEntity.ok(add);
    }

    //질문 목록 조회
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestions() {
        List<QuestionResponseDto> all = questionService.getAllQuestions();
        return ResponseEntity.ok(all);
    }

    //질문 상세 조회
    @GetMapping("/questions/{id}")
    public ResponseEntity getQuestionById(@PathVariable Long id){
        QuestionResponseDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    //질문 수정
    @PutMapping("/questions/{id}")
    public ResponseEntity updateQuestion(@RequestBody QuestionRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal User user){
        QuestionResponseDto update = questionService.updateQuestion(id,requestDto, user);
        return ResponseEntity.ok(update);

    }

    //질문 삭제
    @DeleteMapping("/questions/{id}")
    public ResponseEntity deleteQuestion(@PathVariable Long id, @AuthenticationPrincipal User user){
        questionService.deleteQuestion(id, user);
        return ResponseEntity.ok().build();
    }

    //내 질문 조회
    @GetMapping("/questions/my")
    public ResponseEntity<List<QuestionResponseDto>> getMyQuestions(@AuthenticationPrincipal(expression = "#this") User user){
        List<QuestionResponseDto> questions = questionService.getMyQuestions(user);
        return ResponseEntity.ok(questions);
    }
}
