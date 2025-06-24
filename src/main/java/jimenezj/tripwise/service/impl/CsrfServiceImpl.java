package jimenezj.tripwise.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jimenezj.tripwise.service.CsrfService;
import org.springframework.stereotype.Service;

import java.util.UUID;

// This service handles CSRF protection by generating, validating, and managing CSRF tokens
@Service
public class CsrfServiceImpl implements CsrfService {
    // Name of the CSRF cookie
    public static final String CSRF_COOKIE_NAME = "csrfToken";

    // Generates a new CSRF token using UUID
    public String generateCsrfToken() {
        return UUID.randomUUID().toString();
    }

    // Adds a CSRF token as a cookie in the HTTP response
    @Override
    public void addCsrfTokenCookie(HttpServletResponse response, String csrfToken) {
        Cookie csrfCookie = new Cookie(CSRF_COOKIE_NAME, csrfToken);
        csrfCookie.setHttpOnly(false); // CSRF token should be accessible via JavaScript
        csrfCookie.setSecure(true); // Cookie is only sent over HTTPS
        csrfCookie.setPath("/"); // Cookie is valid for the entire application
        csrfCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days expiration
        csrfCookie.setAttribute("SameSite", "None"); // Allow cross-site requests
        response.addCookie(csrfCookie); // Add the CSRF cookie to the response
    }


    // Validates the CSRF token by comparing the token in the request header with the one in the cookie
    @Override
    public boolean isCsrfTokenValid(HttpServletRequest request) {
        String headerToken = request.getHeader("X-CSRF-Token");
        String cookieToken = null;

        // Check if the CSRF token is present in the request header
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (CSRF_COOKIE_NAME.equals(cookie.getName())) {
                    cookieToken = cookie.getValue(); // Get the CSRF token from the cookie
                    break;
                }
            }
        }
        // Validate the CSRF token by comparing the header token with the cookie token
        return headerToken != null && headerToken.equals(cookieToken);

    }
}
