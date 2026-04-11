package hackerton.educationentity.application.dto.request;

public record UpdateEvaluationRequest(
        Long sessionId,
        Long studentId,
        String dataRef,
        String memo
) {
}
