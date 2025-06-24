package jimenezj.tripwise.service.impl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jimenezj.tripwise.service.HttpOnlyCookieService;
import org.springframework.stereotype.Service;

// This service handles the management of HTTP-only cookies, specifically for refresh tokens
@Service
public class HttpOnlyCookieServiceImpl implements HttpOnlyCookieService {
    // Adds a refresh token as a secure, HTTP-only cookie
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // Prevent JavaScript access to the cookie
        cookie.setSecure(true);// Ensure the cookie is only sent over HTTPS
        cookie.setPath("/");// Set the path for the cookie to be accessible across the application
        cookie.setMaxAge(60 * 60 * 24 * 7); //7 days
        cookie.setAttribute("SameSite", "None"); // Allow cross-site requests
        response.addCookie(cookie);// Add the cookie to the response
    }

    // Clears the refresh token cookie by setting its max age to 0
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}
