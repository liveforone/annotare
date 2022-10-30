package annotare.annotare.comment.model;

import annotare.annotare.board.model.Board;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "board_id")
    private Board board;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  //게시글과 달리 시간까지 포함해야하기에 localdatetime을 사용

    @Builder
    public Comment(Long id, String content, String writer, Board board) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.board = board;
    }
}
