package hackerton.educationentity.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;

public record ShareGuidelineRequest(
        @JsonAlias("share_audience")
        GuidelineAudience audience,
        @JsonAlias("teacher_review_note")
        String teacherReviewNote
) {
}
