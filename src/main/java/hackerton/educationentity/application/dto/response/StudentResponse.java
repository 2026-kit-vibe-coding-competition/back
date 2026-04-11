package hackerton.educationentity.application.dto.response;

public record StudentResponse(
        Long id,
        Long classroomId,
        String name,
        String phone,
        String status
) {
}
