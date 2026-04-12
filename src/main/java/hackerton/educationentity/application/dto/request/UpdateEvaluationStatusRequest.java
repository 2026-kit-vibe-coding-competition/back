package hackerton.educationentity.application.dto.request;

import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;

import java.time.LocalDateTime;

public record UpdateEvaluationStatusRequest(
        EvaluationStatus status,
        LocalDateTime analyzingExpireTime
) {
}
