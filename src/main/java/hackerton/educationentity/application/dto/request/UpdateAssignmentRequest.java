package hackerton.educationentity.application.dto.request;

public record UpdateAssignmentRequest(
        Long sessionId,
        String title,
        String description,
        String formRef,
        Boolean optional
) {
}
