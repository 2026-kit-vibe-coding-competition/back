package hackerton.educationentity.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FeedbackReportResponse(
        Long feedbackReportId,
        Long aiRequestId,
        Long guidelineId,
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
        List<Double> trend,
        String summary,
        String strength,
        String improvement,
        String nextStep,
        String schoolAction,
        String homeAction,
        String nextCheck,
        GuidelineStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
