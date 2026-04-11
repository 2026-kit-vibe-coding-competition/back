package hackerton.educationentity.application.dto.request;

public record CreateClassroomRequest(
        Long schoolId,
        Long teacherId,
        String grade,
        String room
) {
}
