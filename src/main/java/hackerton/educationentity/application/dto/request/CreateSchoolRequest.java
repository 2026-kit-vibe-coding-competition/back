package hackerton.educationentity.application.dto.request;

public record CreateSchoolRequest(
        String name,
        String address,
        String phone,
        String type
) {
}
