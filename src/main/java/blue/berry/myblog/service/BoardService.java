package blue.berry.myblog.service;

import blue.berry.myblog.dto.board.BoardRequest;
import blue.berry.myblog.model.board.Board;
import blue.berry.myblog.model.board.BoardQueryRepository;
import blue.berry.myblog.model.board.BoardRepository;
import blue.berry.myblog.model.user.User;
import blue.berry.myblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveInDTO saveInDTO, Long userId) {
        try {
            // 1. 유저 존재 확인(신뢰할 수 있는 데이터를 받아와야 함(phantom 데이터가 저장되는 걸 방지))
            User userPS = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("유저를 찾을 수 없습니다.")
            );

            // 2. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS));
        } catch (Exception e) {
            throw new RuntimeException("글쓰기 실패 : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true) // 변경 감지 방지, 고립성(repeatable read)
    public Page<Board> 글목록보기(int page) { // CSR은 DTO로 변경해서 돌려줘야 함
        // 1. 모든 전략은 Lazy : 필요할 때만 가져오기 위해서
        // 2. 필요할 때는 직접 fetch join을 작성할 것(방법 중에 in query나 left outer join 발생시키는 것보다 성능이 탁월한 편이라)
        return boardQueryRepository.findAll(page);
    }
}
