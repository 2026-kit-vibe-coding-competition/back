package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.UserDto;
import hackerton.educationentity.domain.school.entity.School;
import hackerton.educationentity.domain.school.repository.SchoolRepository;
import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDto.SignUpDtoRequest request) {
        validateDuplicateEmail(request.getEmail());

        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("School not found"));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                school,
                request.getRole(),
                request.getName(),
                request.getEmail(),
                encodedPassword
        );

        userRepository.save(user);
    }

    public User login(UserDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        validatePassword(request.getPassword(), user.getPassword());

        return user;
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("Password mismatch");
        }
    }
}
