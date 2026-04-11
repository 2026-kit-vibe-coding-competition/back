package hackerton.educationentity.application.dto.request;

public record UpdateSessionRequest(
        Long subjectId,
        String title,
        String date
) {
}
