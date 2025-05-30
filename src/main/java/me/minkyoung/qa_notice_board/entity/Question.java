package me.minkyoung.qa_notice_board.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="question")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();
}
