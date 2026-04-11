package hackerton.educationentity.application.dto.response;

import hackerton.educationentity.domain.user.entity.UserRole;

public record UserResponse(
        Long id,
        Long schoolId,
        UserRole role,
        String name,
        String email
) {
}
