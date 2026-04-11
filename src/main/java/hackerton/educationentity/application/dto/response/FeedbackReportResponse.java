package hackerton.educationentity.application.dto.response;

import java.util.List;

public record FeedbackReportResponse(
        Long id,
        Long aiRequestId,
        Long sessionId,
        Long studentId,
        String studentName,
        String subject,
        String lessonTopic,
        Integer understandingLevel,
        Integer assignmentLevel,
        List<String> weaknessTags,
        String teacherMemo,
        String ocrSummary,
        List<Double> trend
) {
}
