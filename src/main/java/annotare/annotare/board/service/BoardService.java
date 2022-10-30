package annotare.annotare.board.service;

import annotare.annotare.board.model.Board;
import annotare.annotare.board.model.BoardDto;
import annotare.annotare.board.repository.BoardRepository;
import annotare.annotare.user.model.Users;
import annotare.annotare.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findBoardAll(pageable);
    }

    public Board getDetail(Long id) {
        return boardRepository.findOneById(id);
    }

    @Transactional
    public Long saveBoard(String writer, BoardDto boardDto) {
        Users users = userRepository.findByEmail(writer);

        boardDto.setUsers(users);

        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public void updateView(Long id) {
        boardRepository.updateView(id);
    }

    @Transactional
    public void updateGood(Long id) {
        boardRepository.updateGood(id);
    }

    @Transactional
    public Long editBoard(Long id, BoardDto boardDto) {
        Board board = boardRepository.findOneById(id);

        boardDto.setId(board.getId());
        boardDto.setGood(board.getGood());
        boardDto.setView(board.getView());
        boardDto.setUsers(board.getUsers());

        return boardRepository.save(boardDto.toEntity()).getId();
    }
}
