package hackerton.educationentity.application.dto.request;

public record CreateSubjectRequest(
        Long classroomId,
        Long teacherId,
        String name
) {
}
