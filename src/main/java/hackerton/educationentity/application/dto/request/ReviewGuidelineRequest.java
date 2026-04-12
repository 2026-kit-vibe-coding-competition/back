package hackerton.educationentity.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ReviewGuidelineRequest(
        @JsonAlias("teacher_review_note")
        String teacherReviewNote
) {
}
