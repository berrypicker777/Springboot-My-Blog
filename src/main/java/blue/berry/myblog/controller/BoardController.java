package blue.berry.myblog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {

    // RestAPI 주소 설계 규칙에서 자원에는 복수를 붙이는 게 정석(ex. boards)
    @GetMapping({"/", "/board"})
    public String main() {
        return "board/main";
    }
}
