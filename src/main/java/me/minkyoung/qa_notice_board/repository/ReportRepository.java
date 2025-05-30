package me.minkyoung.qa_notice_board.repository;

import me.minkyoung.qa_notice_board.domain.ReportType;
import me.minkyoung.qa_notice_board.entity.Report;
import me.minkyoung.qa_notice_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByTypeAndTargetIdAndReporter(ReportType type, Long targetId, User reporter); //중복 신고 방지
    List<Report> findByType(ReportType type);
    Optional<Report> findById(Long id);
    void delete(Report report);
}
