package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.response.FeedbackReportResponse;
import hackerton.educationentity.domain.feedback_report.entity.FeedbackReport;
import hackerton.educationentity.domain.feedback_report.repository.FeedbackReportRepository;
import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.repository.GuidelineRepository;
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
    private final GuidelineRepository guidelineRepository;

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
        Guideline guideline = guidelineRepository.findByAiRequestId(report.getAiRequest().getId()).orElse(null);

        return new FeedbackReportResponse(
                report.getId(),
                report.getAiRequest().getId(),
                guideline != null ? guideline.getId() : null,
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
                report.getTrend(),
                guideline != null ? guideline.getSummary() : null,
                guideline != null ? guideline.getStrength() : null,
                guideline != null ? guideline.getImprovement() : null,
                guideline != null ? guideline.getNextStep() : null,
                guideline != null ? guideline.getSchoolAction() : null,
                guideline != null ? guideline.getHomeAction() : null,
                guideline != null ? guideline.getNextCheck() : null,
                guideline != null ? guideline.getStatus() : null,
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}
