package me.minkyoung.qa_notice_board.repository;

import me.minkyoung.qa_notice_board.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long id);

}
