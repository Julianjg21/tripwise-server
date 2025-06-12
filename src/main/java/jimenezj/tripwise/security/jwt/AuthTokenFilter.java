package jimenezj.tripwise.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

    // Inject dependencies
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    // Filter executed once per request to check JWT token
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {

        String token = extractToken(request); // Extract Bearer token from the header

        if (token != null) {
            try {
                // Reject if the token is not of type "access"
                String tokenType = jwtUtils.extractTokenType(token);
                if (!"access".equals(tokenType)) {
                    throw new BadCredentialsException("Invalid token for authentication");
                }

                // Extract username (email) from the token
                String username = jwtUtils.extractUsername(token);

                // Load user details from DB
                var userDetails = userDetailsService.loadUserByUsername(username);

                // Validate token against user details
                if (jwtUtils.validateToken(token, userDetails)) {
                    // If valid, create authentication object and set it in the security context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception ex) {
                // If any error occurs return 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }

    // Helper method to extract token from "Authorization" header
    private String extractToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        return (headerAuth != null && headerAuth.startsWith("Bearer ")) ? headerAuth.substring(7) : null;
    }
}


