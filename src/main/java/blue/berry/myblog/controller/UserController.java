package blue.berry.myblog.controller;

import blue.berry.myblog.core.auth.MyUserDetails;
import blue.berry.myblog.core.exception.ssr.Exception400;
import blue.berry.myblog.core.exception.ssr.Exception403;
import blue.berry.myblog.dto.user.UserRequest;
import blue.berry.myblog.model.user.User;
import blue.berry.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final HttpSession session;
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

    @GetMapping("/s/user/{id}/updateProfileForm")
    public String profileUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        // 1. 권한 체크
        if (id != myUserDetails.getUser().getId()) {
            throw new Exception403("권한이 없습니다");
        }
        User userPS = userService.회원프로필보기(id);
        model.addAttribute("user", userPS);
        return "user/profileUpdateForm";
    }

    @PostMapping("/s/user/{id}/updateProfile")
    public String profileUpdate(
            @PathVariable Long id,
            MultipartFile profile,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        // 1. 권한 체크
        if (id != myUserDetails.getUser().getId()) {
            throw new Exception403("권한이 없습니다");
        }

        // 2. 사진 파일 유효성 검사
        if (profile.isEmpty()) {
            throw new Exception400("profile", "사진이 전송되지 않았습니다");
        }

        // 3. 사진을 파일에 저장하고 그 경로를 DB에 저장
        User userPS = userService.프로필사진수정(profile, id);

        // 4. 세션 동기화
        myUserDetails.setUser(userPS);
        session.setAttribute("sessionUser", userPS);

        return "redirect:/";
    }
}