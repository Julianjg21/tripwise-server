package jimenezj.tripwise.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    // Secret key used for signing JWT tokens.
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;
    // Expiration time for tokens
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;         // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    // Generate JWT Access Token with claim "type = access"
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return buildToken(claims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    // Generate JWT Refresh Token with claim "type = refresh"
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }

    // Builds and signs the token using the secret key and expiration
    private String buildToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)                         // Custom claims
                .setSubject(subject)                       // Username (email) of the user
                .setIssuedAt(new Date())                   // Token creation date
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Expiration
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // Sign token
                .compact();                                // Create compact JWT string
    }

    // Extracts username (subject) from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extracts the custom "type" claim (access or refresh)
    public String extractTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("type", String.class);
    }

    // Validates token by checking:
    // 1. Username matches
    // 2. Token is not expired
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
