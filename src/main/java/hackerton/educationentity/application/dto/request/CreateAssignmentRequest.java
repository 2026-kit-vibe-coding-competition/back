package hackerton.educationentity.application.dto.request;

public record CreateAssignmentRequest(
        Long sessionId,
        String title,
        String description,
        String formRef,
        Boolean optional
) {
}
