package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.response.FeedbackReportResponse;
import hackerton.educationentity.domain.feedback_report.entity.FeedbackReport;
import hackerton.educationentity.domain.feedback_report.repository.FeedbackReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackReportService {
    private final FeedbackReportRepository feedbackReportRepository;

    public List<FeedbackReportResponse> getFeedbackReports(Long sessionId, Long studentId) {
        return feedbackReportRepository.search(sessionId, studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public FeedbackReportResponse getFeedbackReport(Long id) {
        FeedbackReport report = feedbackReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FeedbackReport not found"));

        return toResponse(report);
    }

    private FeedbackReportResponse toResponse(FeedbackReport report) {
        return new FeedbackReportResponse(
                report.getId(),
                report.getAiRequest().getId(),
                report.getSession().getId(),
                report.getStudent().getId(),
                report.getStudentName(),
                report.getSubject(),
                report.getLessonTopic(),
                report.getUnderstandingLevel(),
                report.getAssignmentLevel(),
                report.getWeaknessTags(),
                report.getTeacherMemo(),
                report.getOcrSummary(),
                report.getTrend()
        );
    }
}
