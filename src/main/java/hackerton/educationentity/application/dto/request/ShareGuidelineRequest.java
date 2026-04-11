package hackerton.educationentity.application.dto.request;

import hackerton.educationentity.domain.guideline.type.GuidelineAudience;

public record ShareGuidelineRequest(
        GuidelineAudience audience,
        String teacherReviewNote
) {
}
