package hackerton.educationentity.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ParentLatestGuidelineResponse(
        Long studentId,
        String studentName,
        Long guidelineId,
        String priorityGap,
        String summary,
        String strength,
        String improvement,
        String nextStep,
        String schoolAction,
        String homeAction,
        String nextCheck,
        GuidelineStatus status,
        LocalDateTime sharedAt
) {
}
