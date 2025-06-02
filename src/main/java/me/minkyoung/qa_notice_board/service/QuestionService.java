package me.minkyoung.qa_notice_board.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.domain.Role;
import me.minkyoung.qa_notice_board.dto.QuestionRequestDto;
import me.minkyoung.qa_notice_board.dto.QuestionResponseDto;
import me.minkyoung.qa_notice_board.entity.Question;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.QuestionRepository;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    //질문 생성
    public QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, User user) {
        //Dto에서 제목과 내용 꺼내기
        String title = questionRequestDto.getTitle();
        String content = questionRequestDto.getContent();

        //질문 엔터티 생성, 임시 객체
        Question question =Question.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        //저장
       Question savedQuestion = questionRepository.save(question);

        //응답 Dto로 변환
        QuestionResponseDto questionResponseDto = new QuestionResponseDto(
                savedQuestion.getId(),
                savedQuestion.getTitle(),
                savedQuestion.getContent(),
                savedQuestion.getUser().getNickname(),
                savedQuestion.getCreatedAt(),
                savedQuestion.getUpdatedAt(),
                savedQuestion.getUser().getEmail()
        );

        return questionResponseDto;

    }

    //질문 전체 조회
    public List<QuestionResponseDto> getAllQuestions(){
        //모든 질문 조회 및 리스트처리, 생성일 기준 정렬
        List<Question> questions = questionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        //응답dto로 변환
        List<QuestionResponseDto> questionResponseDto = questions.stream()
                       .map(q-> new QuestionResponseDto(
                               q.getId(),
                               q.getTitle(),
                               q.getContent(),
                               q.getUser().getNickname(),
                               q.getCreatedAt(),
                               q.getUpdatedAt(),
                               q.getUser().getEmail()
                       ))
                       .collect((Collectors.toList()));

        return questionResponseDto;


    }

    //질문 상세 조회
    public QuestionResponseDto getQuestionById(Long id) {
        //ID로 질문 엔터티 조회
        Question question = questionRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //조회한 질문 Dto로 변환
        QuestionResponseDto questionResponseDto = new QuestionResponseDto(question.getId(), question.getTitle(), question.getContent(),
                question.getUser().getNickname(),question.getCreatedAt(), question.getUpdatedAt(),question.getUser().getEmail());

        return questionResponseDto;
    }

    //질문 삭제
    public QuestionResponseDto deleteQuestion(Long id, User user){
        //질문 조회
        Question question = questionRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //권한 확인
        boolean isWriter = question.getUser().getId().equals(user.getId()); //작성자 본인 확인
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN; //관리자인지 확인
        if(!isAdmin && !isWriter){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        questionRepository.delete(question);
        return new QuestionResponseDto(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getUser().getNickname(),
                question.getCreatedAt(),
                question.getUpdatedAt(),
                question.getUser().getEmail()
        );
    }

    //질문 수정
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto questionRequestDto, User user){
        //질문 조회
        Question question = questionRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //작성자 본인 확인
        if(!question.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        //제목과 내용 수정
        question.setTitle(questionRequestDto.getTitle());
        question.setContent(questionRequestDto.getContent());

        //수정된 결과 DTO로 변환
        return new QuestionResponseDto(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getUser().getNickname(),
                question.getCreatedAt(),
                question.getUpdatedAt(),
                question.getUser().getEmail()
        );
    }

    //내 질문만 가져오기
    public List<QuestionResponseDto> getMyQuestions(User user){
        List<Question> questions = questionRepository.findAllByUser_Email(user.getEmail());
        return questions.stream()
                .map(QuestionResponseDto::new)
                .collect(Collectors.toList());
    }
}
