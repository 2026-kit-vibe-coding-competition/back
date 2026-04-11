package hackerton.educationentity.application.dto.response;

public record SchoolResponse(
        Long id,
        String name,
        String address,
        String phone,
        String type
) {
}
