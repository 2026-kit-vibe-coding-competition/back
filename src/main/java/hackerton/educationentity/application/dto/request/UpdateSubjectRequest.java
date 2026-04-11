package hackerton.educationentity.application.dto.request;

public record UpdateSubjectRequest(
        Long classroomId,
        Long teacherId,
        String name
) {
}
