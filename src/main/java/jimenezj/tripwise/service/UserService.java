package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.user.UserProfileRequest;
import jimenezj.tripwise.dto.user.UserProfileResponse;

public interface UserService {
    // Get user profile
    UserProfileResponse getUserProfile();

    // Update user profile
    UserProfileResponse updateUserProfile(UserProfileRequest request);
}
