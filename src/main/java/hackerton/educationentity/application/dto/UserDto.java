package hackerton.educationentity.application.dto;

import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class SignUpDtoRequest {
        @NotNull(message = "required")
        private UserRole role;

        @NotBlank(message = "required")
        private String name;

        @Email(message = "invalid email format")
        @NotBlank(message = "required")
        private String email;

        @NotBlank(message = "required")
        private String password;

        @NotNull(message = "required")
        private Long schoolId;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        @Email(message = "invalid email format")
        @NotBlank(message = "required")
        private String email;

        @NotBlank(message = "required")
        private String password;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoginResponse {
        private final Long userId;
        private final String name;
        private final String email;
        private final UserRole role;

        public static LoginResponse from(User user) {
            return new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole());
        }
    }
}
