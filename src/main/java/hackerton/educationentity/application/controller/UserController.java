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
    public ResponseEntity<String> signup(
            @Valid @RequestBody UserDto.SignUpDtoRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(
            @Valid @RequestBody UserDto.LoginRequest request) {
        User user = userService.login(request);

        String accessToken = jwtProvider.createAccessToken(user);

        ResponseCookie cookie = ResponseCookie.from(
                        "accessToken", accessToken) // 이름이 "accessToken"이란 토큰을 만들겠다
                .httpOnly(true) // 브라우저 자바스크립트에서는 이 쿠키를 직접 읽지 못하게 막는다
                .secure(false) // HTTPS가 아니어도 쿠키를 보낼 수 있게 한다
                .path("/") // 이 쿠키를 사이트 전체 경로에서 사용할 수 있게 한다
                .sameSite("Lax")
                .build();

        UserDto.LoginResponse response = UserDto.LoginResponse.from(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                // 응답 헤더에 Set-Cookie를 넣는다
                // Set-Cookie: accessToken=eyJhbGciOiJIUzI1NiJ9...; Path=/; HttpOnly; SameSite=Lax
                .body(response);
    }
}