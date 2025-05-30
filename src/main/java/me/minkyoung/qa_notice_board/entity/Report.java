package me.minkyoung.qa_notice_board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.minkyoung.qa_notice_board.domain.ReportType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType type; //QUESTION, ANSWER

    private Long targetId; // 신고 대상 게시물 ID

    private String reason; //사유

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="reporter_id")
    private User reporter;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

}
