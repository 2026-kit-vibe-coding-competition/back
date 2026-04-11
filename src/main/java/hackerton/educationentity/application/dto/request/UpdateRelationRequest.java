package hackerton.educationentity.application.dto.request;

public record UpdateRelationRequest(
        Long parentId,
        Long studentId,
        String relation
) {
}
