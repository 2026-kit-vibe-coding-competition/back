package hackerton.educationentity.application.dto.request;

public record UpdateClassroomRequest(
        Long schoolId,
        Long teacherId,
        String grade,
        String room
) {
}
