package hackerton.educationentity.application.dto.request;

public record UpdateStudentRequest(
        Long classroomId,
        String name,
        String phone,
        String status
) {
}
