package annotare.annotare.comment.model;

import annotare.annotare.board.model.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String writer;
    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .writer(writer)
                .board(board)
                .build();
    }
}
