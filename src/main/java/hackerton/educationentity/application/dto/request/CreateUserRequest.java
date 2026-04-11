package hackerton.educationentity.application.dto.request;

import hackerton.educationentity.domain.user.entity.UserRole;

public record CreateUserRequest(
        Long schoolId,
        UserRole role,
        String name,
        String email,
        String password
) {
}
