package blue.berry.myblog.service;

import blue.berry.myblog.dto.user.UserRequest;
import blue.berry.myblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // security config에서 빈으로 이미 등록했으므로

    // insert, update, delete -> try catch 처리(save 메서드 내부의 try catch로 잡힌 오류를 내가 다시 try catch로 잡아야 한다.)
    @Transactional
    public void 회원가입(UserRequest.JoinInDTO joinInDTO) {
        try {
            // 1. 패스워드 암호화
            joinInDTO.setPassword(passwordEncoder.encode(joinInDTO.getPassword()));

            // 2. DB 저장
            userRepository.save(joinInDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("회원가입 오류 : " + e.getMessage());
        }
    } // 더티체킹, DB 세션 종료(OSIV = false)
}
