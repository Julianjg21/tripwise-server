package jimenezj.tripwise.dto.auth;

public record AuthTokens(
        String accessToken,
        String refreshToken
) {
}
