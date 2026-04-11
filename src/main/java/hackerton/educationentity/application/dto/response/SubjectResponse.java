package hackerton.educationentity.application.dto.response;

public record SubjectResponse(
        Long id,
        Long classroomId,
        Long teacherId,
        String name
) {
}
