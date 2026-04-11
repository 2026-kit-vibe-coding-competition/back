package hackerton.educationentity.application.dto.response;

public record ClassroomResponse(
        Long id,
        Long schoolId,
        Long teacherId,
        String grade,
        String room
) {
}
