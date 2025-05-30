package me.minkyoung.qa_notice_board.repository;

import me.minkyoung.qa_notice_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
