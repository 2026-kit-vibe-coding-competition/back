package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.UserDto;
import hackerton.educationentity.application.service.UserService;
import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.system.jwt.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDto.SignUpDtoRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok("Signup completed");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        User user = userService.login(request);

        String accessToken = jwtProvider.createAccessToken(user);

        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .build();

        UserDto.LoginResponse response = UserDto.LoginResponse.from(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}
