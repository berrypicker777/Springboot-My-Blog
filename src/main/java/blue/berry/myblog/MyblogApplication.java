package blue.berry.myblog;

import blue.berry.myblog.model.user.User;
import blue.berry.myblog.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MyblogApplication {

    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .status(true)
                    .build();
            userRepository.save(ssar);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }

}
