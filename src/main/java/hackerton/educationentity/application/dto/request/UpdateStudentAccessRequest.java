package hackerton.educationentity.application.dto.request;

import hackerton.educationentity.domain.student_access.type.AccessType;

public record UpdateStudentAccessRequest(
        Long studentId,
        Long teacherId,
        AccessType type
) {
}
