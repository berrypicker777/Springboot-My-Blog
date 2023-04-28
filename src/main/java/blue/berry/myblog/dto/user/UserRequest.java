package blue.berry.myblog.dto.user;

import blue.berry.myblog.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class UserRequest {
    @Getter
    @Setter
    public static class JoinInDTO {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
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
