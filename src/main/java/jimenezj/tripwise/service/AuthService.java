package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.auth.JwtResponse;
import jimenezj.tripwise.dto.auth.LoginRequest;
import jimenezj.tripwise.dto.auth.SignupRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse login(SignupRequest signupRequest);
}
