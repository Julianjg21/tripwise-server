package jimenezj.tripwise.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
