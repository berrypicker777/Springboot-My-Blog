package blue.berry.myblog.controller;

import blue.berry.myblog.dto.user.UserRequest;
import blue.berry.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public String join(UserRequest.JoinInDTO joinInDTO) {
        userService.회원가입(joinInDTO);
        return "redirect:/loginForm"; // req 302
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }
}