package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateUserRequest;
import hackerton.educationentity.application.dto.request.UpdateUserRequest;
import hackerton.educationentity.application.dto.response.UserResponse;
import hackerton.educationentity.domain.school.entity.School;
import hackerton.educationentity.domain.school.repository.SchoolRepository;
import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.entity.UserRole;
import hackerton.educationentity.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserManagementService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getUsers(Long schoolId, UserRole role, String name, String email) {
        return userRepository.search(schoolId, role, name, email).stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUser(Long id) {
        return toResponse(getUserEntity(id));
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        School school = getSchool(request.schoolId());

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = User.builder()
                .school(school)
                .role(request.role())
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = getUserEntity(id);
        School school = getSchool(request.schoolId());

        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        user.setSchool(school);
        user.setRole(request.role());
        user.setName(request.name());
        user.setEmail(request.email());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserEntity(id);
        userRepository.delete(user);
    }

    private User getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private School getSchool(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School not found"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getSchool().getId(),
                user.getRole(),
                user.getName(),
                user.getEmail()
        );
    }
}
