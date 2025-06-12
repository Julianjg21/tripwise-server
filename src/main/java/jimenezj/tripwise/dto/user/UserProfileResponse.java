package jimenezj.tripwise.dto.user;

public record UserProfileResponse(
        long id,
        String email,
        String fullName
) {
}
