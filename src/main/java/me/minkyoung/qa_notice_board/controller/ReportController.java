package me.minkyoung.qa_notice_board.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.domain.ReportType;
import me.minkyoung.qa_notice_board.dto.ReportRequestDto;
import me.minkyoung.qa_notice_board.dto.ReportResponseDto;
import me.minkyoung.qa_notice_board.entity.Report;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports/")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReportResponseDto> addReport(@RequestBody ReportRequestDto dto, @AuthenticationPrincipal User user) {
        ReportResponseDto result = reportService.addReport(dto,user);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportResponseDto>> getAllReports(@RequestParam ReportType type) {
        List<ReportResponseDto> result = reportService.getAllReports(type);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDto> deleteReport(@PathVariable Long id) {
        ReportResponseDto result = reportService.deleteReport(id);
        return ResponseEntity.ok(result);
    }
}
