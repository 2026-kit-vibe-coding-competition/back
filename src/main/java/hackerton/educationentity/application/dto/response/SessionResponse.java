package hackerton.educationentity.application.dto.response;

public record SessionResponse(
        Long id,
        Long subjectId,
        String title,
        String date
) {
}
