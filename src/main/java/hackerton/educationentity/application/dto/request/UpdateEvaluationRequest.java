package hackerton.educationentity.application.dto.request;

import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;

import java.time.LocalDateTime;

public record UpdateEvaluationRequest(
        Long sessionId,
        Long studentId,
        String dataRef,
        String memo,
        EvaluationStatus status,
        LocalDateTime analyzingExpireTime
) {
}
