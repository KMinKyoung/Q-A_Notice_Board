package me.minkyoung.qa_notice_board.repository;

import me.minkyoung.qa_notice_board.entity.Question;
import me.minkyoung.qa_notice_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long id);
    List<Question> findAllByUser_Email(String email);
}
