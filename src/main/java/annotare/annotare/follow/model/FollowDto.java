package annotare.annotare.follow.model;

import annotare.annotare.user.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowDto {

    private Long id;
    private Users users;
    private String following;

    public Follow toEntity() {
        return Follow.builder()
                .id(id)
                .users(users)
                .following(following)
                .build();
    }
}
