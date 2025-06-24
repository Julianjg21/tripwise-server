package jimenezj.tripwise.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Interface for CSRF protection service
public interface CsrfService {
    // Adds a CSRF token cookie to the response
    void addCsrfTokenCookie(HttpServletResponse response, String csrfToken);
    // Validates the CSRF token from the request
    boolean isCsrfTokenValid(HttpServletRequest request);
    // Generates a new CSRF token
    String generateCsrfToken();
}
