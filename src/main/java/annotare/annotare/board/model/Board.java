package annotare.annotare.board.model;

import annotare.annotare.user.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String category;

    @Column(columnDefinition = "integer default 0")
    private int view;

    @Column(columnDefinition = "integer default 0")
    private int good;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    @JsonBackReference
    private Users users;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    @Builder
    public Board(Long id, String title, String content, String category, int view, int good, Users users) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.view = view;
        this.good = good;
        this.users = users;
    }
}
