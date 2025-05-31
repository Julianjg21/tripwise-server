package jimenezj.tripwise.service.impl;


import jimenezj.tripwise.dto.auth.JwtResponse;
import jimenezj.tripwise.dto.auth.LoginRequest;
import jimenezj.tripwise.dto.auth.SignupRequest;
import jimenezj.tripwise.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse login(SignupRequest signupRequest) {
        return null;
    }
}
