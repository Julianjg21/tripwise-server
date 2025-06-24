package jimenezj.tripwise.security.csrf;

import org.springframework.security.web.util.matcher.RequestMatcher;
import jakarta.servlet.http.HttpServletRequest;

// This class implements RequestMatcher to define which endpoints are protected by CSRF.
public class CsrfProtectedEndpoints implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        // This method checks if the request matches the criteria for CSRF protection.
        return request.getRequestURI().equals("/api/auth/refresh") // Matches the refresh token endpoint
                && request.getMethod().equals("POST");// Ensures the method is POST
    }
}