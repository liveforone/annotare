package annotare.annotare.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {  //pw를 빼고 넘겨주기위해서

    private Long id;
    private String email;
    private Role auth;

    @Builder
    public UserResponseDto(Long id, String email, Role auth) {
        this.id = id;
        this.email = email;
        this.auth = auth;
    }
}
