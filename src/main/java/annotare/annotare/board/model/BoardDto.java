package annotare.annotare.board.model;

import annotare.annotare.user.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String category;
    private int view;
    private int good;
    private Users users;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .view(view)
                .good(good)
                .users(users)
                .build();
    }
}
