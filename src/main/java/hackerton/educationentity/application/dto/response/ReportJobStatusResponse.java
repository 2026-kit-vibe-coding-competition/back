package hackerton.educationentity.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReportJobStatusResponse(
        String id,
        Long studentId,
        Long sessionId,
        String status,
        Long guidelineId,
        String errorMessage,
        LocalDateTime updatedAt
) {
}
