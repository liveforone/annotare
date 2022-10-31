package annotare.annotare.comment.service;

import annotare.annotare.board.model.Board;
import annotare.annotare.board.repository.BoardRepository;
import annotare.annotare.comment.model.Comment;
import annotare.annotare.comment.model.CommentDto;
import annotare.annotare.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Page<Comment> getCommentList(Long boardId, Pageable pageable) {
        return commentRepository.findCommentList(boardId, pageable);
    }

    @Transactional
    public void saveComment(Long boardId, CommentDto commentDto, String writer) {
        Board board = boardRepository.findOneById(boardId);

        commentDto.setBoard(board);
        commentDto.setWriter(writer);

        commentRepository.save(commentDto.toEntity());
    }
}
