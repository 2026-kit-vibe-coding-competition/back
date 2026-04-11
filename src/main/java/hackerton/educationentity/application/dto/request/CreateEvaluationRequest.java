package hackerton.educationentity.application.dto.request;

public record CreateEvaluationRequest(
        Long sessionId,
        Long studentId,
        String dataRef,
        String memo
) {
}
