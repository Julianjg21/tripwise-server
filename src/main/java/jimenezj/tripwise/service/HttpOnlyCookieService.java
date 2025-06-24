package jimenezj.tripwise.service;

import jakarta.servlet.http.HttpServletResponse;

// Interface for managing HttpOnly cookies, specifically for refresh tokens
public interface HttpOnlyCookieService {
  // Adds a HttpOnly cookie for the refresh token
  void addRefreshTokenCookie(HttpServletResponse response, String refreshToken);

  // Clears the HttpOnly cookie for the refresh token
  void clearRefreshTokenCookie(HttpServletResponse response);
}
