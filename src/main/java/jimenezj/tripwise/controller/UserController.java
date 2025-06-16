package jimenezj.tripwise.controller;

import jakarta.validation.Valid;
import jimenezj.tripwise.dto.user.UserProfileRequest;
import jimenezj.tripwise.dto.user.UserProfileResponse;
import jimenezj.tripwise.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/me")
public class UserController {
    
    //Injecting user service
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Get user profile by authenticated user id
    @GetMapping()
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
    
    //update user profile by  authenticated user id
    @PutMapping
    public ResponseEntity<UserProfileResponse> updateUserProfile(@RequestBody @Valid UserProfileRequest request){
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }

}
