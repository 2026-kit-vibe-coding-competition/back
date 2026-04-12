package hackerton.educationentity.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GuidelineResponse(
        Long guidelineId,
        Long aiRequestId,
        Long sessionId,
        Long studentId,
        String priorityGap,
        String summary,
        String strength,
        String improvement,
        String nextStep,
        String schoolAction,
        String homeAction,
        String nextCheck,
        GuidelineAudience audience,
        GuidelineStatus status,
        String teacherReviewNote,
        Long feedbackReportId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
