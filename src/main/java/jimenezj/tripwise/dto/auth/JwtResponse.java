package jimenezj.tripwise.dto.auth;

import jimenezj.tripwise.dto.user.UserDTO;

public record JwtResponse(
        String token,
    UserDTO userData                                                       
) {
}
