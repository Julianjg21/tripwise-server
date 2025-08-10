package jimenezj.tripwise.config;

import jimenezj.tripwise.security.csrf.CsrfProtectedEndpoints;
import jimenezj.tripwise.security.jwt.AuthTokenFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import jimenezj.tripwise.security.CustomAuthenticationEntryPoint;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthTokenFilter authTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Value("${security.cors.allowed-origins}")
    String corsAllowedOrigin;

    // Constructor injection for dependencies
    public SecurityConfig(AuthenticationProvider authenticationProvider, AuthTokenFilter authTokenFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.authTokenFilter = authTokenFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    // Configures the security filter chain for the application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf  // Enable CSRF protection
                        .requireCsrfProtectionMatcher(new CsrfProtectedEndpoints()) // Custom matcher for CSRF protection
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Use cookie-based CSRF token repository
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(corsAllowedOrigin));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                .exceptionHandling(exception -> exception // Handle authentication exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                ) 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                            "/api/password-reset/**") // Public endpoints that do not require authentication
                                            .permitAll()
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .authenticationProvider(authenticationProvider) // Set the custom authentication provider
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
