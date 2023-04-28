package blue.berry.myblog.service;

import blue.berry.myblog.core.exception.csr.ExceptionApi400;
import blue.berry.myblog.core.exception.ssr.Exception400;
import blue.berry.myblog.core.exception.ssr.Exception500;
import blue.berry.myblog.core.util.MyFileUtil;
import blue.berry.myblog.dto.user.UserRequest;
import blue.berry.myblog.model.user.User;
import blue.berry.myblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // security config에서 빈으로 이미 등록했으므로

    @Value("${file.path}") // dev.yml에 설정(배포할때 본코드를 고칠 필요없이 yml만 수정)
    private String uploadFolder;

    // insert, update, delete -> try catch 처리(save 메서드 내부의 try catch로 잡힌 오류를 내가 다시 try catch로 잡아야 한다.)
    @Transactional
    public void 회원가입(UserRequest.JoinInDTO joinInDTO) {
        // 1. 유저 중복 확인(밖에다 빼놔야 500에 안잡힌다.)
        Optional<User> userOP = userRepository.findByUsername(joinInDTO.getUsername());
        if (userOP.isPresent()) {
            // 로그 필요(비정상적인 접근)
            throw new Exception400("username", "유저네임이 중복되었어요");
        }
        try {
            // 2. 패스워드 암호화
            joinInDTO.setPassword(passwordEncoder.encode(joinInDTO.getPassword()));

            // 3. DB 저장
            userRepository.save(joinInDTO.toEntity());
        } catch (Exception e) {
            throw new Exception500("회원가입 실패 : " + e.getMessage());
        }
    } // 더티체킹, DB 세션 종료(OSIV = false)

    public User 회원프로필보기(Long id) {
        User userPS = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("id", "해당 유저가 존재하지 않습니다"));
        return userPS;
    }

    @Transactional
    public User 프로필사진수정(MultipartFile profile, Long id) {
        try {
            String uuidImageName = MyFileUtil.write(uploadFolder, profile);
            User userPS = userRepository.findById(id)
                    .orElseThrow(() -> new Exception500("로그인 된 유저가 DB에 존재하지 않음"));
            userPS.changeProfile(uuidImageName);
            return userPS;
        } catch (Exception e) {
            throw new Exception500("프로필 사진 등록 실패 : " + e.getMessage());
        }
    }

    public void 유저네임중복체크(String username) {
        Optional<User> userOP = userRepository.findByUsername(username);
        if (userOP.isPresent()) {
            throw new ExceptionApi400("username", "유저네임이 중복되었어요");
        }
    }
}
