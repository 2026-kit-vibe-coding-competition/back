package hackerton.educationentity.application.dto.response;

public record AssignmentResponse(
        Long id,
        Long sessionId,
        String title,
        String description,
        String formRef,
        boolean optional
) {
}
