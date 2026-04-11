package hackerton.educationentity.application.dto.response;

import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;

public record GuidelineResponse(
        Long id,
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
        String teacherReviewNote
) {
}
