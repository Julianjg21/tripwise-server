package jimenezj.tripwise.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jimenezj.tripwise.dto.reset_password.CreatePasswordResetRequest;
import jimenezj.tripwise.dto.reset_password.PasswordResetRequest;
import jimenezj.tripwise.exception.BadRequestException;
import jimenezj.tripwise.model.PasswordResetToken;
import jimenezj.tripwise.model.User;
import jimenezj.tripwise.repository.PasswordResetRepository;
import jimenezj.tripwise.repository.UserRepository;
import jimenezj.tripwise.service.PasswordResetService;
import jimenezj.tripwise.utils.HtmlTemplateLoader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final HtmlTemplateLoader htmlTemplateLoader;

    // Client link for the reset password page
     @Value("${client.reset-password-link}")
     String resetPasswordClientLink;

    // Constructor injection for dependencies
    public PasswordResetServiceImpl(UserRepository userRepository, PasswordResetRepository passwordResetRepository,
                                    PasswordEncoder passwordEncoder, JavaMailSender mailSender, HtmlTemplateLoader htmlTemplateLoader) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.htmlTemplateLoader = htmlTemplateLoader;
    }

    // Method to create a password reset request
    @Override
    public void createResetPassword(CreatePasswordResetRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            String token = generateSecureToken();
            int expirationMinutes = 30;
            LocalDateTime expiration = LocalDateTime.now().plusMinutes(expirationMinutes);

            PasswordResetToken resetToken = new PasswordResetToken(user, token, expiration);
            passwordResetRepository.save(resetToken);
            sendResetEmail(user.getEmail(), token);
        });

        // No exception is thrown if the email does not exist, to prevent information
        // leakage
    }

    // Method to reset the password using a token and new password
    @Override
    public void resetPassword(PasswordResetRequest request) {

        // Validate the token
        PasswordResetToken resetToken = passwordResetRepository.findByToken(request.token())
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        // Check if the token has expired
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid token");
        }

        // Update the user's password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // Save the updated user and delete the reset token
        userRepository.save(user);
        passwordResetRepository.delete(resetToken); // Token is erased after using it
    }


    // Method to send the reset password email
    private void sendResetEmail(String email, String token) {
        try {
            // Construct the reset link using the client link and token
            String resetLink = resetPasswordClientLink + token;
            // Load the HTML template and replace the placeholder with the reset link
            String html = htmlTemplateLoader.loadTemplate("templates/email/reset-password.html");
            String body = html.replace("${resetLink}", resetLink);

            // Create and send the email using JavaMailSender
            MimeMessage message = mailSender.createMimeMessage();
            // Use MimeMessageHelper to set the email properties
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Restore your password");
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Error sending mail:" + e);
        }
    }

    // Method to generate a secure random token
    public String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
