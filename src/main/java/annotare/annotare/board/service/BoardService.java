package annotare.annotare.board.service;

import annotare.annotare.board.model.Board;
import annotare.annotare.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findBoardAll(pageable);
    }
}
