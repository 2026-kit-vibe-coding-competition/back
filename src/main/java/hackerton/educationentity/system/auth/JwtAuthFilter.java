package hackerton.educationentity.system.auth;

import hackerton.educationentity.system.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String ACCESS_TOKEN_COOKIE = "accessToken";
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/users/signup",
            "/api/v1/users/login",
            "/error",
            "/favicon.ico"
    );

    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || PUBLIC_PATHS.contains(request.getServletPath());
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token == null || token.isBlank() || !jwtProvider.isValidToken(token)) {
            writeUnauthorized(response);
            return;
        }

        request.setAttribute("authUserId", jwtProvider.getUserId(token));
        request.setAttribute("authUserRole", jwtProvider.getUserRole(token));
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (ACCESS_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Unauthorized\"}");
    }
}

