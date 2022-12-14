package annotare.annotare.follow.model;

import annotare.annotare.user.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "users_id")
    private Users users;  //나

    private String following;  //내가 팔로우 하는 사람.

    @Builder
    public Follow(Long id, Users users, String following) {
        this.id = id;
        this.users = users;
        this.following = following;
    }
}
