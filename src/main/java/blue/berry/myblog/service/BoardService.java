package blue.berry.myblog.service;

import blue.berry.myblog.core.exception.ssr.Exception400;
import blue.berry.myblog.core.util.MyParseUtil;
import blue.berry.myblog.dto.board.BoardRequest;
import blue.berry.myblog.model.board.Board;
import blue.berry.myblog.model.board.BoardQueryRepository;
import blue.berry.myblog.model.board.BoardRepository;
import blue.berry.myblog.model.user.User;
import blue.berry.myblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

            // 2. 썸네일 만들기
            String thumbnail = MyParseUtil.getThumbnail(saveInDTO.getContent());

            // 3. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS, thumbnail));
        } catch (Exception e) {
            throw new RuntimeException("글쓰기 실패 : " + e.getMessage());
        }
    }

    /*
        isEmpty() 메소드는 문자열의 길이가 0인지 검사합니다.
        isBlank() 메소드는 문자열이 비어있거나 공백 문자열(whitespace-only string)인지 검사합니다.
    */
    @Transactional(readOnly = true) // 변경 감지 방지, 고립성(repeatable read)
    public Page<Board> 글목록보기(int page, String keyword) { // CSR은 DTO로 변경해서 돌려줘야 함
        if (keyword.isBlank()) {
            return boardQueryRepository.findAll(page);
        } else {
            Page<Board> boardPGPS = boardQueryRepository.findAllByKeyword(page, keyword);
            return boardPGPS; // osiv false라서 리턴된 후에는 PS를 빼도 됨
        }
        // 1. 모든 전략은 Lazy : 필요할 때만 가져오기 위해서
        // 2. 필요할 때는 직접 fetch join을 작성할 것(방법 중에 in query나 left outer join 발생시키는 것보다 성능이 탁월한 편이라)
    }

    public Board 게시글상세보기(Long id) {
        Board boardPS = boardRepository.findByIdFetchUser(id).orElseThrow(
                () -> new Exception400("id", "게시글 아이디를 찾을 수 없습니다")
        );
        // 1. Lazy Loading 하는 것 보다 join fetch 하는 것이 좋다.
        // 2. 왜 Lazy를 쓰냐면, 쓸데 없는 조인 쿼리를 줄이기 위해서이다.
        // 3. 사실 @ManyToOne은 Eager 전략을 쓰는 것이 좋다.
        // boardPS.getUser().getUsername();
        return boardPS;
    }
}
