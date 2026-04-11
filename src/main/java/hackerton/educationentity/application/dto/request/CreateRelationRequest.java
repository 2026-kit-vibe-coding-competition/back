package hackerton.educationentity.application.dto.request;

public record CreateRelationRequest(
        Long parentId,
        Long studentId,
        String relation
) {
}
