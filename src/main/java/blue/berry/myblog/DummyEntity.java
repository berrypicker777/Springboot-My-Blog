package blue.berry.myblog;

import blue.berry.myblog.model.board.Board;
import blue.berry.myblog.model.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyEntity {

    protected User newUser(String username, BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username + "@nate.com")
                .role("USER")
                .profile("person.png")
                .build();
    }

    protected Board newBoard(String title, User user) {
        return Board.builder()
                .title(title)
                .content(title + "에 대한 내용입니다")
                .user(user)
                .thumbnail("/upload/person.png")
                .build();
    }
}