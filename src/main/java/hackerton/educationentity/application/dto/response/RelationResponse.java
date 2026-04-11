package hackerton.educationentity.application.dto.response;

public record RelationResponse(
        Long id,
        Long parentId,
        Long studentId,
        String relation
) {
}
