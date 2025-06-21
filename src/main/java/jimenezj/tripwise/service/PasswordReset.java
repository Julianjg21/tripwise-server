package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.reset_password.CreatePasswordResetRequest;
import jimenezj.tripwise.dto.reset_password.PasswordResetRequest;

// This interface defines the contract for password reset operations.
public interface PasswordReset {

     // Method to create a password reset request
     void createResetPassword(CreatePasswordResetRequest request);

     // Method to reset the password using a token and new password
     void resetPassword(PasswordResetRequest request);
}
