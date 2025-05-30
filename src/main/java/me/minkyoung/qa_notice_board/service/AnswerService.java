package me.minkyoung.qa_notice_board.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.domain.Role;
import me.minkyoung.qa_notice_board.dto.AnswerRequestDto;
import me.minkyoung.qa_notice_board.dto.AnswerResponseDto;
import me.minkyoung.qa_notice_board.entity.Answer;
import me.minkyoung.qa_notice_board.entity.Question;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.AnswerRepository;
import me.minkyoung.qa_notice_board.repository.QuestionRepository;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    //질문에 대한 답변 작성
    public AnswerResponseDto addAnswer(AnswerRequestDto answerRequestDto, User user, Long questionId) {
        //1. 질문 존재 확인
        Question question = questionRepository.findById(questionId).orElseThrow(()->new IllegalArgumentException("해당 질문이 존재하지 않습니다"));
        //2. 답변 엔터티 생성
        Answer answer = new Answer(answerRequestDto.getContent(), question,user);
        //3. 작성자인지 확인
        String nickname= user.getId().equals(question.getUser().getId())?user.getNickname()+"(작성자)":user.getNickname();

        //4. 저장 및 응답 Dto 반환
            Answer saved = answerRepository.save(answer);
             return new AnswerResponseDto(
                     saved.getId(),
                     saved.getContent(),
                     nickname,
                     saved.getQuestion().getId(),
                     saved.getCreatedAt(),
                     saved.getUpdatedAt()
             );

    }

    //답변 수정
    public AnswerResponseDto updateAnswer(AnswerRequestDto answerRequestDto, User user, Long id) {
        // 답변 조회
        Answer answer = answerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("답변이 존재하지 않습니다."));
        // 답변을 작성한 유저가 맞는지 확인
        if(!answer.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        // 내용 수정
        answer.setContent(answerRequestDto.getContent());

        //닉넹미 작성자 여부 확인
        boolean isAuthor = answer.getQuestion().getUser().getId().equals(user.getId());
        String nickname = isAuthor?user.getNickname()+"(작성자)":user.getNickname();

        // 응답 dto 반환
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                nickname,
                answer.getQuestion().getId(),
                answer.getCreatedAt(),
                answer.getUpdatedAt()
        );
    }

    //답변 삭제(관리자도 가능)
    public AnswerResponseDto deleteAnswer(Long id, User user) {
        //존재하는 답변인지 확인
        Answer answer = answerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 답변입니다."));

        // 내용 삭제(만약 관리자일 경우에도 삭제)
        if(!answer.getUser().getId().equals(user.getId())&& user.getRole() !=Role.ROLE_ADMIN) {
           throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        //삭제 전 정보 저장
        String nickname = answer.getQuestion().getUser().getId().equals(user.getId())?user.getNickname()+"(작성자)":user.getNickname();

        //삭제
        answerRepository.delete(answer);

        //응답 dto반환
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                nickname,
                answer.getQuestion().getId(),
                answer.getCreatedAt(),
                answer.getUpdatedAt()
        );

    }

    //질문에 달린 전체 답변 조회, 비회원도 답변 확인가능
    public List<AnswerResponseDto> getAllAnswers(Long questionId) {
        //질문 객체를 생성하여 질문 게시물 존재 확인
        Question question = questionRepository.findById(questionId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));
        //질문에 달린 답변 리스트 조회
        List<Answer> answers= answerRepository.findByQuestionId(questionId);
        //질문에 대한 답변을 리스트 형태로 응답 dto반환
        return answers.stream()
                .map(answer -> new AnswerResponseDto(
                        answer.getId(),
                        answer.getContent(),
                        answer.getUser().getNickname(),
                        answer.getQuestion().getId(),
                        answer.getCreatedAt(),
                        answer.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        }
    }

    //답변에 대한 대댓글
    //public AnswerResponseDto getAnswer() {

    //}




