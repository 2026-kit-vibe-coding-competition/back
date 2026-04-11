package hackerton.educationentity.system.auth;

import hackerton.educationentity.domain.school.entity.School;
import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.entity.UserRole;
import hackerton.educationentity.system.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthFilterTest {
    private MockMvc mockMvc;
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider("12345678901234567890123456789012", 3_600_000L);
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtProvider);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .addFilters(jwtAuthFilter)
                .build();
    }

    @Test
    void publicLoginEndpointBypassesAuth() throws Exception {
        mockMvc.perform(post("/api/users/login"))
                .andExpect(status().isOk())
                .andExpect(content().string("login"));
    }

    @Test
    void protectedEndpointReturnsUnauthorizedWhenCookieMissing() throws Exception {
        mockMvc.perform(get("/protected"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void protectedEndpointReturnsUnauthorizedWhenTokenInvalid() throws Exception {
        mockMvc.perform(get("/protected").cookie(new jakarta.servlet.http.Cookie("accessToken", "invalid-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void protectedEndpointAcceptsValidCookieAndSetsAuthAttributes() throws Exception {
        String token = jwtProvider.createAccessToken(createUser());

        mockMvc.perform(get("/protected").cookie(new jakarta.servlet.http.Cookie("accessToken", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userRole").value("TEACHER"));
    }

    @Test
    void optionsRequestBypassesAuth() throws Exception {
        mockMvc.perform(options("/protected"))
                .andExpect(status().isOk());
    }

    private User createUser() {
        School school = new School();
        school.setId(10L);

        User user = new User(school, UserRole.TEACHER, "tester", "tester@example.com", "encoded-password");
        user.setId(1L);
        return user;
    }

    @RestController
    private static class TestController {
        @PostMapping("/api/users/login")
        String login() {
            return "login";
        }

        @GetMapping("/protected")
        Map<String, Object> protectedEndpoint(HttpServletRequest request) {
            return Map.of(
                    "userId", request.getAttribute("authUserId"),
                    "userRole", request.getAttribute("authUserRole")
            );
        }

        @RequestMapping(value = "/protected", method = RequestMethod.OPTIONS)
        String options() {
            return "options";
        }
    }
}
