package hackerton.educationentity.application.dto.request;

public record CreateSessionRequest(
        Long subjectId,
        String title,
        String date
) {
}
