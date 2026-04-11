package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.response.FeedbackReportResponse;
import hackerton.educationentity.application.service.FeedbackReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback-reports")
public class FeedbackReportController {
    private final FeedbackReportService feedbackReportService;

    @GetMapping
    public List<FeedbackReportResponse> getFeedbackReports(@RequestParam(required = false) Long sessionId,
                                                           @RequestParam(required = false) Long studentId) {
        return feedbackReportService.getFeedbackReports(sessionId, studentId);
    }

    @GetMapping("/{id}")
    public FeedbackReportResponse getFeedbackReport(@PathVariable Long id) {
        return feedbackReportService.getFeedbackReport(id);
    }
}
