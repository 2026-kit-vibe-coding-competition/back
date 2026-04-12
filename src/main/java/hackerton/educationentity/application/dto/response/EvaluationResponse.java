package hackerton.educationentity.application.dto.response;

import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;

import java.time.LocalDateTime;

public record EvaluationResponse(
        Long id,
        Long sessionId,
        Long studentId,
        String dataRef,
        String memo,
        EvaluationStatus status,
        LocalDateTime analyzingExpireTime
) {
}
