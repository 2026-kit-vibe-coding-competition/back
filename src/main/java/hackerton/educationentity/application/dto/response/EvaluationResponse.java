package hackerton.educationentity.application.dto.response;

public record EvaluationResponse(
        Long id,
        Long sessionId,
        Long studentId,
        String dataRef,
        String memo
) {
}
