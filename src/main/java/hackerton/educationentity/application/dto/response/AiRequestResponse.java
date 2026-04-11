package hackerton.educationentity.application.dto.response;

import hackerton.educationentity.domain.ai_request.type.AiRequestStatus;
import hackerton.educationentity.domain.ai_request.type.AiRequestType;

public record AiRequestResponse(
        Long id,
        Long sessionId,
        Long studentId,
        String jobId,
        AiRequestType requestType,
        AiRequestStatus status,
        Long feedbackReportId,
        Long guidelineId
) {
}
