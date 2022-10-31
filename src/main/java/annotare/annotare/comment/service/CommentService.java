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

    public Comment getComment(Long id) {
        return commentRepository.findOneById(id);
    }

    @Transactional
    public void saveComment(Long boardId, CommentDto commentDto, String writer) {
        Board board = boardRepository.findOneById(boardId);

        commentDto.setBoard(board);
        commentDto.setWriter(writer);

        commentRepository.save(commentDto.toEntity());
    }

    @Transactional
    public Long editComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findOneById(id);

        commentDto.setId(comment.getId());
        commentDto.setWriter(comment.getWriter());
        commentDto.setBoard(comment.getBoard());

        return commentRepository.save(commentDto.toEntity()).getBoard().getId();
    }
}
