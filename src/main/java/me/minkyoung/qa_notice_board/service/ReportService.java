package me.minkyoung.qa_notice_board.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.domain.ReportType;
import me.minkyoung.qa_notice_board.dto.ReportRequestDto;
import me.minkyoung.qa_notice_board.dto.ReportResponseDto;
import me.minkyoung.qa_notice_board.entity.Report;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.QuestionRepository;
import me.minkyoung.qa_notice_board.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final QuestionRepository questionRepository;

    // 신고추가(회원만 가능)
    public ReportResponseDto addReport(ReportRequestDto reportRequestDto, User user) {
        //댓글 또는 답변이 존재하는지 확인, 없을경우 "존재하지 않는 글입니다"
        if(reportRequestDto.getType() == ReportType.QUESTION){
           if(!questionRepository.existsById(reportRequestDto.getTargetId())){
               throw new IllegalArgumentException("존재하지 않는 질문입니다.");
           }
        } else if(reportRequestDto.getType() == ReportType.ANSWER){
            if(!questionRepository.existsById(reportRequestDto.getTargetId())) {
                throw new IllegalArgumentException("존재하지 않은 답변입니다.");
            }
        } else{
            throw new IllegalArgumentException("잘못된 신고입니다.");
        }
        //신고 중복 확인
        boolean alreadyReported = reportRepository.existsByTypeAndTargetIdAndReporter(reportRequestDto.getType(), reportRequestDto.getTargetId(), user);
        if(alreadyReported){
            throw new IllegalArgumentException("이미 신고한 게시물입니다.");
        }
        //저장 및 dto 반환
        Report report = new Report();
        report.setType(reportRequestDto.getType());
        report.setTargetId(reportRequestDto.getTargetId());
        report.setReason(reportRequestDto.getReason());
        report.setReporter(user);

        Report saved = reportRepository.save(report);
        return new ReportResponseDto(
                saved.getId(),
                saved.getType(),
                saved.getTargetId(),
                saved.getReason(),
                saved.getReporter(),
                saved.getCreatedAt()
        );
    }

    // 신고확인(관리자만)
    public List<ReportResponseDto> getAllReports(ReportType type) {
        //신고내역 조회
        List<Report> reports = reportRepository.findByType(type);
        //list형태로 dto 반환
        return reports.stream()
                .map(report -> new ReportResponseDto(
                        report.getId(),
                        report.getType(),
                        report.getTargetId(),
                        report.getReason(),
                        report.getReporter(),
                        report.getCreatedAt()
                ))
                .collect(Collectors.toList());

    }

    // 신고내역 삭제(관리자만)
    public ReportResponseDto deleteReport(Long reportId){
        //삭제 대상 신고 조회
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고입니다."));
        //삭제 저장
        reportRepository.delete(report);
        //dto 반환
        return new ReportResponseDto(
                report.getId(),
                report.getType(),
                report.getTargetId(),
                report.getReason(),
                report.getReporter(),
                report.getCreatedAt()
        );
    }
}
