package hackerton.educationentity.application.dto.response;

import hackerton.educationentity.domain.student_access.type.AccessType;

public record StudentAccessResponse(
        Long id,
        Long studentId,
        Long teacherId,
        AccessType type
) {
}
