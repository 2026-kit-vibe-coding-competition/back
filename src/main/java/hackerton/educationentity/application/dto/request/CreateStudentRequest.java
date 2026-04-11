package hackerton.educationentity.application.dto.request;

public record CreateStudentRequest(
        Long classroomId,
        String name,
        String phone,
        String status
) {
}
