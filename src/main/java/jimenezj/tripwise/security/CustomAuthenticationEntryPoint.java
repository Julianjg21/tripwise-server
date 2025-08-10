// src/main/java/jimenezj/tripwise/security/CustomAuthenticationEntryPoint.java
package jimenezj.tripwise.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    // Handles unauthorized access attempts
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Sends a 401 Unauthorized error response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}