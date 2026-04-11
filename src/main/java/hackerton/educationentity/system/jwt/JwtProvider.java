package hackerton.educationentity.system.jwt;

import hackerton.educationentity.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long accessTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expire-time}") long accessTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
