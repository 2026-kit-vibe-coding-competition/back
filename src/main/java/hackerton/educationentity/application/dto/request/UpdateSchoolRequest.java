package hackerton.educationentity.application.dto.request;

public record UpdateSchoolRequest(
        String name,
        String address,
        String phone,
        String type
) {
}
