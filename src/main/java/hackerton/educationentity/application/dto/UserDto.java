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
        @NotNull(message = "필수입니다.")
        private UserRole role;

        @NotBlank(message = "필수입니다.")
        private String name;

        @Email(message = "메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "필수입니다.")
        private String email;

        @NotBlank(message = "필수입니다.")
        private String password;

        @NotNull(message = "필수입니다.")
        private Long schoolId; // 어느 학교 소속인지
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        @Email(message = "메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "필수입니다.")
        private String email;

        @NotBlank(message = "필수입니다.")
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

