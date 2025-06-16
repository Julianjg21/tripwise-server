package jimenezj.tripwise.service.impl;

import jimenezj.tripwise.dto.user.UserProfileRequest;
import jimenezj.tripwise.dto.user.UserProfileResponse;
import jimenezj.tripwise.exception.BadRequestException;
import jimenezj.tripwise.model.User;
import jimenezj.tripwise.repository.UserRepository;
import jimenezj.tripwise.security.impl.UserDetailsServiceImpl;
import jimenezj.tripwise.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserRepository userRepository;

    // Injecting dependencies
    public UserServiceImpl(UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.userDetailsServiceImpl = userDetailsService;
        this.userRepository = userRepository;
    }

    // Get user profile by authenticated user
    @Override
    public UserProfileResponse getUserProfile() {
        User user = userDetailsServiceImpl.getAuthenticatedUser().getUser(); // Get authenticated user
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getFullName());
    }

    // update user profile by authenticated user id
    @Override
    public UserProfileResponse updateUserProfile(UserProfileRequest request) {
        // Validate request
        if (request.email() == null || request.email().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        User user = userDetailsServiceImpl.getAuthenticatedUser().getUser(); // Get authenticated user

        // Search email of the request received in the database
        Optional<User> userByEmail = userRepository.findByEmail(request.email());

        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(user.getId())) {
            throw new BadRequestException("That email is already in use by another user");
        }

        // Build user with the new data
        user.setEmail(request.email());
        user.setFullName(request.fullName());

        User updatedUser = userRepository.save(user);

        return new UserProfileResponse(updatedUser.getId(), updatedUser.getEmail(), updatedUser.getFullName());
    }

}
