package blue.berry.myblog.dto.user;

import blue.berry.myblog.model.user.User;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {
    @Getter
    @Setter
    public static class JoinInDTO {
        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role("USER")
                    .status(true)
                    .profile("person.png")
                    .build();
        }
    }
}
